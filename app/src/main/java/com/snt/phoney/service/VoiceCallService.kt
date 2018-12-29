package com.snt.phoney.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import android.view.SurfaceView
import cn.jiguang.jmrtc.api.JMRtcClient
import cn.jiguang.jmrtc.api.JMRtcListener
import cn.jiguang.jmrtc.api.JMRtcSession
import cn.jiguang.jmrtc.api.JMSignalingMessage
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.callback.RequestCallback
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.snt.phoney.BuildConfig
import com.snt.phoney.ICallStateListener
import com.snt.phoney.IVoiceCallService
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.domain.model.JMUser
import com.snt.phoney.ui.voicecall.VoiceAnswerActivity
import com.snt.phoney.utils.data.Constants
import dagger.android.AndroidInjection
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val TAG = "VoiceCallService"

const val Method_onCallOutgoing = 1
const val Method_onCallInviteReceived = 2
const val Method_onCallOtherUserInvited = 3
const val Method_onCallConnected = 4
const val Method_onCallMemberJoin = 5
const val Method_onCallMemberOffline = 6
const val Method_onCallDisconnected = 7
const val Method_onCallError = 8

const val ERROR_TIMEOUT = 872002

const val REASON_HANGUP = 11//JMRtcClient.DisconnectReason.hangup.ordinal
const val REASON_REFUSE = 12//JMRtcClient.DisconnectReason.refuse.ordinal
const val REASON_CANCEL = 13//JMRtcClient.DisconnectReason.cancel.ordinal
const val REASON_BUSY = 14//JMRtcClient.DisconnectReason.busy.ordinal
const val REASON_OFFLINE = 15//JMRtcClient.DisconnectReason.offline.ordinal

class VoiceCallService : Service() {

    companion object {
        @JvmStatic
        private val URL = "ws://${Constants.Api.DOMAIN_NAME}/im/websocket"
    }

    private var mWebSocket: WebSocket? = null
    private var mVoiceCallWebSocketListener: VoiceCallWebSocketListener? = VoiceCallWebSocketListener()

    private val mJMRtcListenerImpl = JMRtcListenerImpl()

    @Inject
    lateinit var mUserAccessor: UserAccessor

    private var isConnected = false
    private var isConnecting = false

    override fun onBind(intent: Intent): IBinder {
        return VoiceCallServiceImpl()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        JMRtcClient.getInstance().initEngine(mJMRtcListenerImpl)
    }

    inner class JMRtcListenerImpl : JMRtcListener() {
        override fun onCallOutgoing(session: JMRtcSession) {
            Log.d("TTTT", "xxxxxxxxxxxx onCallOutgoing session=$session")
            isConnecting = true
            notifyListener(Method_onCallOutgoing)
        }

        override fun onCallConnected(session: JMRtcSession, surfaceView: SurfaceView) {
            Log.d("TTTT", "xxxxxxxxxxxx onCallConnected session=$session")
            isConnected = true
            isConnecting = false
            notifyListener(Method_onCallConnected)
        }

        override fun onCallOtherUserInvited(userInfo: UserInfo?, userList: MutableList<UserInfo>?, session: JMRtcSession?) {
            val user = if (userInfo != null) JMUser.from(userInfo) else null
            notifyListener(Method_onCallOtherUserInvited, user = user)
        }

        /**
         * 通话过程中，有用户退出通话
         */
        override fun onCallMemberOffline(userInfo: UserInfo, reason: JMRtcClient.DisconnectReason) {
            Log.d("TTTT", "xxxxxxxxxxxx onCallMemberOffline user=$userInfo  reason=$reason")
            val user = if (userInfo != null) JMUser.from(userInfo) else null
            notifyListener(Method_onCallMemberOffline, user = user, reason = reasonToCode(reason))
        }

        override fun onCallMemberJoin(userInfo: UserInfo?, surfaceView: SurfaceView?) {
            val user = if (userInfo != null) JMUser.from(userInfo) else null
            notifyListener(Method_onCallMemberJoin, user = user)
        }

        override fun onCallDisconnected(reason: JMRtcClient.DisconnectReason) {
            Log.d("TTTT", "xxxxxxxxxxxx onCallDisconnected reason=$reason")
            isConnected = false
            isConnecting = false
            notifyListener(Method_onCallDisconnected, reason = reasonToCode(reason))
        }

        override fun onCallError(errorCode: Int, desc: String) {
            Log.d("TTTT", "xxxxxxxxxxxx onCallError errorCode=$errorCode  desc=$desc")
            isConnected = false
            isConnecting = false
            if (errorCode == 872004 || errorCode == 872106) {
                JMRtcClient.getInstance().initEngine(mJMRtcListenerImpl)
            }
            if (errorCode == 872002) {//time out

            }
            notifyListener(Method_onCallError, errorCode = errorCode, errorDesc = desc)
        }

        override fun onCallInviteReceived(session: JMRtcSession) {
            Log.d("TTTT", "xxxxxxxxxxxx onCallInviteReceived session=$session")
            JMRtcClient.getInstance().session.getInviterUserInfo(object : RequestCallback<UserInfo>() {
                override fun gotResult(responseCode: Int, responseMessage: String?, result: UserInfo?) {
                    val user = if (result != null) JMUser.from(result) else null
                    Log.d("TTTT", "ttttttttt user=$user")
                    notifyListener(Method_onCallInviteReceived, user = user)
                    startVoiceCallActivity(user)
                }
            })
        }

        private fun reasonToCode(reason: JMRtcClient.DisconnectReason): Int {
            return when (reason) {
                JMRtcClient.DisconnectReason.hangup -> REASON_HANGUP
                JMRtcClient.DisconnectReason.refuse -> REASON_REFUSE
                JMRtcClient.DisconnectReason.cancel -> REASON_CANCEL
                JMRtcClient.DisconnectReason.busy -> REASON_BUSY
                JMRtcClient.DisconnectReason.offline -> REASON_OFFLINE
            }
        }
    }

    private fun notifyListener(methodId: Int, user: JMUser? = null, reason: Int = 0, errorCode: Int = 0, errorDesc: String? = null) {
        val size = mCallStateListeners.beginBroadcast()
        for (i in 0 until size) {
            try {
                when (methodId) {
                    Method_onCallOutgoing -> mCallStateListeners.getBroadcastItem(i).onCallOutgoing()
                    Method_onCallInviteReceived -> mCallStateListeners.getBroadcastItem(i).onCallInviteReceived(user)
                    Method_onCallOtherUserInvited -> mCallStateListeners.getBroadcastItem(i).onCallOtherUserInvited(user)
                    Method_onCallConnected -> mCallStateListeners.getBroadcastItem(i).onCallConnected()
                    Method_onCallMemberJoin -> mCallStateListeners.getBroadcastItem(i).onCallMemberJoin(user)
                    Method_onCallMemberOffline -> mCallStateListeners.getBroadcastItem(i).onCallMemberOffline(user, reason)
                    Method_onCallDisconnected -> mCallStateListeners.getBroadcastItem(i).onCallDisconnected(reason)
                    Method_onCallError -> mCallStateListeners.getBroadcastItem(i).onCallError(errorCode, errorDesc)
                }
            } catch (e: RemoteException) {
                Log.e(TAG, "error:${e.message}")
            }
        }
        mCallStateListeners.finishBroadcast()
    }

    fun startVoiceCallActivity(user: JMUser?) {
        val intent = Intent(this, VoiceAnswerActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            user?.let { putExtra("user", it) }
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        JMRtcClient.getInstance().releaseEngine()
    }


    private fun hangup() {
        JMRtcClient.getInstance().hangup(object : BasicCallback() {
            override fun gotResult(code: Int, message: String?) {
                Log.d("TTTT", "xxxxxxxx hangup  hangup  hangup  code=$code  message=$message")
            }
        })
    }

    fun call(user: JMUser) {
        connect()
        JMessageClient.getUserInfo(user.username, object : GetUserInfoCallback() {
            override fun gotResult(responseCode: Int, responseMessage: String?, user: UserInfo?) {
                user?.let { user ->
                    JMRtcClient.getInstance().call(listOf(user), JMSignalingMessage.MediaType.AUDIO, object : BasicCallback() {
                        override fun gotResult(code: Int, message: String?) {
                            Log.d("TTTT", "xxxxxxxx call  call  call  code=$code  message=$message")

                        }
                    })
                }
            }
        })
    }

    private fun accept() {
        JMRtcClient.getInstance().accept(object : BasicCallback() {
            override fun gotResult(code: Int, message: String?) {
                Log.d("TTTT", "xxxxxxxx accept  accept  accept  code=$code  message=$message")
            }
        })
    }

    private fun refuse() {
        JMRtcClient.getInstance().refuse(object : BasicCallback() {
            override fun gotResult(code: Int, message: String?) {
                Log.d("TTTT", "xxxxxxxx refuse  refuse  refuse  code=$code  message=$message")
            }
        })
    }


    private fun enableSpeaker(enable: Boolean) {
        JMRtcClient.getInstance().enableSpeakerphone(enable)
    }


    val mCallStateListeners = RemoteCallbackList<ICallStateListener>()

    inner class VoiceCallServiceImpl : IVoiceCallService.Stub() {

        override fun isConnected(): Boolean {
            return this@VoiceCallService.isConnected
        }

        override fun isConnecting(): Boolean {
            return this@VoiceCallService.isConnecting
        }

        override fun call(user: JMUser) {
            this@VoiceCallService.call(user)
        }

        override fun hangup() {
            this@VoiceCallService.hangup()
        }

        override fun refuse() {
            this@VoiceCallService.refuse()
        }

        override fun accept() {
            this@VoiceCallService.accept()
        }

        override fun enableSpeaker(enable: Boolean) {
            this@VoiceCallService.enableSpeaker(enable)
        }

        override fun registerICallStateListener(listener: ICallStateListener?) {
            mCallStateListeners.register(listener)
        }

        override fun unregisterICallStateListener(listener: ICallStateListener?) {
            mCallStateListeners.unregister(listener)
        }
    }


    private fun connect() {
        val token = mUserAccessor.getAccessToken() ?: return
        val clientBuilder = OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        val client = clientBuilder.build()
        val request = Request.Builder().url(buildUrl(token)).build()
        client.newWebSocket(request, mVoiceCallWebSocketListener)
        client.dispatcher().executorService().shutdown()
    }

    private fun buildUrl(token: String): String {
        return "$URL/token=$token"
    }


    inner class VoiceCallWebSocketListener : WebSocketListener() {
        /**
         * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
         * messages.
         */
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("TTTT", "onOpen============>$response")
            mWebSocket = webSocket

        }

        /** Invoked when a text (type `0x1`) message has been received.  */
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("TTTT", "onMessage 1============>$text")
        }

        /** Invoked when a binary (type `0x2`) message has been received.  */
        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("TTTT", "onMessage 2============>$bytes")
        }

        /**
         * Invoked when the remote peer has indicated that no more incoming messages will be
         * transmitted.
         */
        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("TTTT", "onMessage 2============>code=$code   reason=$reason")
        }

        /**
         * Invoked when both peers have indicated that no more messages will be transmitted and the
         * connection has been successfully released. No further calls to this listener will be made.
         */
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("TTTT", "onMessage 2============>code=$code   reason=$reason")
        }

        /**
         * Invoked when a web socket has been closed due to an error reading from or writing to the
         * network. Both outgoing and incoming messages may have been lost. No further calls to this
         * listener will be made.
         */
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("TTTT", "onMessage 2============>t=$t   response=$response")
        }


    }

}
