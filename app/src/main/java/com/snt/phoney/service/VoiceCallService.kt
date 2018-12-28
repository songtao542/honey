package com.snt.phoney.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.view.SurfaceView
import cn.jiguang.jmrtc.api.JMRtcClient
import cn.jiguang.jmrtc.api.JMRtcListener
import cn.jiguang.jmrtc.api.JMRtcSession
import cn.jiguang.jmrtc.api.JMSignalingMessage
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.snt.phoney.BuildConfig
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.extensions.TAG
import com.snt.phoney.ui.voicecall.VoiceAnswerActivity
import com.snt.phoney.utils.data.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object MessageEvent {
    const val EVENT_CONNECTING = 11
    const val EVENT_CONNECTED = 12
    const val EVENT_DISCONNECTING = 13
    const val EVENT_DISCONNECTED = 14
    const val EVENT_OFFLINE = 15
    const val EVENT_INVITED = 16
    const val EVENT_ERROR = 17
    const val EVENT_STATE = 18

    const val CMD_CALL = 21
    const val CMD_ACCEPT = 22
    const val CMD_HANGUP = 23
    const val CMD_QUERY_STATE = 24

    const val STATE_CONNECTED = 31
    const val STATE_NOT_CONNECTED = 32
}

class VoiceCallService : Service(), Handler.Callback {

    companion object {
        @JvmStatic
        private val URL = "ws://${Constants.Api.DOMAIN_NAME}/im/websocket"
    }

    private var mWebSocket: WebSocket? = null
    private var mVoiceCallWebSocketListener: VoiceCallWebSocketListener? = VoiceCallWebSocketListener()

//    private val mBinder: VoiceCallBinder = VoiceCallBinder()
//    inner class VoiceCallBinder : Binder() {
//        fun getService(): VoiceCallService {
//            return this@VoiceCallService
//        }
//    }

    @Inject
    lateinit var mUserAccessor: UserAccessor

    private val mHandler = Handler(this)

    private var mReplyToMessenger: Messenger? = null

    private var mConnected = false

    override fun onBind(intent: Intent): IBinder {
        return Messenger(mHandler).binder
    }

    override fun handleMessage(msg: Message?): Boolean {
        msg?.let { message ->
            when (message.what) {
                MessageEvent.CMD_CALL -> {
                    try {
                        mReplyToMessenger = message.replyTo
                        call(message.obj as String)
                    } catch (e: Exception) {
                        Log.e(TAG, "error:${e.message}")
                    }
                    return true
                }
                MessageEvent.CMD_ACCEPT -> {
                    mReplyToMessenger = message.replyTo
                    accept()
                    return true
                }
                MessageEvent.CMD_HANGUP -> {
                    mReplyToMessenger = message.replyTo
                    hangupIfConnected()
                    return true
                }
                MessageEvent.CMD_QUERY_STATE -> {
                    mReplyToMessenger = message.replyTo
                    val state = if (mConnected) MessageEvent.STATE_CONNECTED else MessageEvent.STATE_NOT_CONNECTED
                    sendMessage(MessageEvent.EVENT_STATE, state)
                    return true
                }
                else -> {
                    return false
                }
            }
        }
        return false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        JMRtcClient.getInstance().initEngine(object : JMRtcListener() {
            override fun onCallOutgoing(session: JMRtcSession) {
                Log.d("TTTT", "xxxxxxxxxxxx onCallOutgoing session=$session")
                mConnected = true
                sendMessage(MessageEvent.EVENT_CONNECTING)
            }

            override fun onCallConnected(session: JMRtcSession, surfaceView: SurfaceView) {
                Log.d("TTTT", "xxxxxxxxxxxx onCallConnected session=$session")
                mConnected = true
                sendMessage(MessageEvent.EVENT_CONNECTED)
            }

            /**
             * 通话过程中，有用户退出通话
             */
            override fun onCallMemberOffline(user: UserInfo, reason: JMRtcClient.DisconnectReason) {
                Log.d("TTTT", "xxxxxxxxxxxx onCallMemberOffline user=$user  reason=$reason")
                sendMessage(MessageEvent.EVENT_OFFLINE)
                hangupIfConnected()
            }

            override fun onCallDisconnected(reason: JMRtcClient.DisconnectReason) {
                Log.d("TTTT", "xxxxxxxxxxxx onCallDisconnected reason=$reason")
                mConnected = false
                sendMessage(MessageEvent.EVENT_DISCONNECTED)
            }

            override fun onCallError(errorCode: Int, desc: String) {
                Log.d("TTTT", "xxxxxxxxxxxx onCallError errorCode=$errorCode  desc=$desc")
                mConnected = false
                sendMessage(MessageEvent.EVENT_ERROR)
            }

            override fun onCallInviteReceived(session: JMRtcSession) {
                Log.d("TTTT", "xxxxxxxxxxxx onCallInviteReceived session=$session")
                mConnected = false
                startVoiceCallActivity()
            }
        })
    }


    fun startVoiceCallActivity() {
        val intent = Intent(this, VoiceAnswerActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }


    private fun sendMessage(what: Int, obj: Any? = null) {
        mReplyToMessenger?.let { messenger ->
            val reply = Message.obtain()
            reply.what = what
            obj?.let { reply.obj = it }
            messenger.send(reply)
            return@let
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hangupIfConnected()
        JMRtcClient.getInstance().releaseEngine()
    }

    private fun hangupIfConnected() {
        sendMessage(MessageEvent.EVENT_DISCONNECTING)
        if (mConnected) {
            hangup()
        } else {
            sendMessage(MessageEvent.EVENT_DISCONNECTED)
        }
    }

    private fun hangup() {
        JMRtcClient.getInstance().hangup(object : BasicCallback() {
            override fun gotResult(code: Int, message: String?) {
                Log.d("TTTT", "xxxxxxxx hangup code=$code  message=$message")
            }
        })
    }

    fun call(username: String) {
        JMessageClient.getUserInfo(username, object : GetUserInfoCallback() {
            override fun gotResult(responseCode: Int, responseMessage: String?, user: UserInfo?) {
                user?.let { user ->
                    val users = ArrayList<UserInfo>().apply {
                        add(user)
                    }
                    JMRtcClient.getInstance().call(users, JMSignalingMessage.MediaType.AUDIO, object : BasicCallback() {
                        override fun gotResult(code: Int, message: String?) {
                            Log.d("TTTT", "xxxxxxxx call code=$code  message=$message")

                        }
                    })
                }
            }
        })
    }

    private fun accept() {
        JMRtcClient.getInstance().accept(object : BasicCallback() {
            override fun gotResult(code: Int, message: String?) {
                Log.d("TTTT", "xxxxxxxx accept code=$code  message=$message")
            }
        })
    }



    private fun enableSpeaker(enable: Boolean) {
        JMRtcClient.getInstance().enableSpeakerphone(enable)
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


    data class State(val state: Int)

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
