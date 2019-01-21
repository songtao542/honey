package com.snt.phoney.service

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.PowerManager
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
import com.snt.phoney.domain.model.JMUser
import com.snt.phoney.ui.voicecall2.VoiceAnswerActivity
import com.snt.phoney.utils.data.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import java.util.concurrent.TimeUnit

const val Method_onCallOutgoing = 1
const val Method_onCallInviteReceived = 2
const val Method_onCallOtherUserInvited = 3
const val Method_onCallConnected = 4
const val Method_onCallMemberJoin = 5
const val Method_onCallMemberOffline = 6
const val Method_onCallDisconnected = 7
const val Method_onCallError = 8

const val ERROR_TIMEOUT = 872002
const val ERROR_REQUEST_FAIL = 648416
const val ERROR_NO_BALANCE = 184555

const val REASON_HANGUP = 11   //JMRtcClient.DisconnectReason.hangup.ordinal
const val REASON_REFUSE = 12   //JMRtcClient.DisconnectReason.refuse.ordinal
const val REASON_CANCEL = 13   //JMRtcClient.DisconnectReason.cancel.ordinal
const val REASON_BUSY = 14     //JMRtcClient.DisconnectReason.busy.ordinal
const val REASON_OFFLINE = 15  //JMRtcClient.DisconnectReason.offline.ordinal


const val STATE_ASK_CALL = 1
const val STATE_START_CALL = 2
const val STATE_CHECK_CALL = 3
const val STATE_ENDING_CALL = 4
const val STATE_ABORT_CALL = 5

const val REQUEST_ASK_CALL_MSG = "94e25b988ae44bc88532ece1ef09ca41"
const val REQUEST_START_CALL_MSG = "e0984b753bda40fafb244f42ed219981"
const val REQUEST_CHECK_CALL_MSG = "344116ec8f12fb7fa1ef131adb2acb74"
const val REQUEST_ENDING_CALL_MSG = "83b718ea1245fd7af052f60d408b0d94"
const val REQUEST_ABORT_CALL_MSG = "c2a291532da49d810e5d63c9974c7385"
@Suppress("unused")
const val REQUEST_HEART_MSG = "b5ddf4a366d03d0c8c0bc756fd6156de"

const val RESPONSE_ASK_CALL_MSG = "f8e449c2aa83536f21e19dd173e17230"
const val RESPONSE_START_CALL_MSG = "c66c549817ea26a4f0202a023841c490"
const val RESPONSE_CHECK_CALL_OK_MSG = "92804ed1595e04be8cdd2c5706477e12"
const val RESPONSE_NO_BALANCE_MSG = "c57a3f655901e8212c625e6c90bda8c6"
const val RESPONSE_NO_BALANCE_MSG1 = "蜜币不足，挂机"
const val RESPONSE_ENDING_CALL_MSG = "eb7a35d3d44d08afbb9d9feaac75e755"
const val RESPONSE_ABORT_CALL_MSG = "22d71560e3f72f92ffc7b87a8bd639f2"
const val RESPONSE_HEART_MSG = "79f595671ecfe67462bea6e312811b07"

class VoiceCallEngine(private val application: Application) {

    companion object {

        const val TAG = "VoiceCallEngine"

        @JvmStatic
        private val URL = "wss://${Constants.Api.DOMAIN_NAME}/im/websocket"

        private var INSTANCE: VoiceCallEngine? = null

        @Synchronized
        fun init(application: Application) {
            if (INSTANCE == null) {
                INSTANCE = VoiceCallEngine(application)
            } else {
                INSTANCE?.initEngine()
            }
        }

        @Synchronized
        fun getInstance(): VoiceCallEngine {
            if (INSTANCE == null) {
                throw RuntimeException("The VoiceCallEngine has not init!")
            }
            return INSTANCE!!
        }

        fun release() {
            if (INSTANCE != null) {
                JMRtcClient.getInstance().releaseEngine()
                INSTANCE?.reset()
            }
        }
    }

    private var mWebSocket: WebSocket? = null
    private var mVoiceCallWebSocketListener: VoiceCallWebSocketListener = VoiceCallWebSocketListener()

    private val mCallStateListeners = ArrayList<CallStateListener>()

    private val mJMRtcListenerImpl = JMRtcListenerImpl()

    private val mHandler = Handler()

    private var isConnected = false
    private var isConnecting = false
    private var isSpeakerEnabled = false
    private var isMute = false

    init {
        initEngine()
    }

    private fun initEngine() {
        JMRtcClient.getInstance().initEngine(mJMRtcListenerImpl)
    }

    inner class JMRtcListenerImpl : JMRtcListener() {
        override fun onCallOutgoing(session: JMRtcSession) {
            Log.d(TAG, "onCallOutgoing==session=$session")
            isConnecting = true
            notifyListener(Method_onCallOutgoing)
        }

        override fun onCallConnected(session: JMRtcSession, surfaceView: SurfaceView) {
            Log.d(TAG, "onCallConnected=session=$session")
            isConnected = true
            isConnecting = false
            startCall()
            notifyListener(Method_onCallConnected)
        }

        override fun onCallOtherUserInvited(userInfo: UserInfo?, userList: MutableList<UserInfo>?, session: JMRtcSession?) {
            Log.d(TAG, "onCallOtherUserInvited=>userInfo=$userInfo")
            val user = if (userInfo != null) JMUser.from(userInfo) else null
            notifyListener(Method_onCallOtherUserInvited, user = user)
        }

        /**
         * 通话过程中，有用户退出通话
         */
        override fun onCallMemberOffline(userInfo: UserInfo?, reason: JMRtcClient.DisconnectReason) {
            Log.d(TAG, "onCallMemberOffline user=$userInfo  reason=$reason")
            val user = if (userInfo != null) JMUser.from(userInfo) else null
            notifyListener(Method_onCallMemberOffline, user = user, reason = reasonToCode(reason))
        }

        override fun onCallMemberJoin(userInfo: UserInfo?, surfaceView: SurfaceView?) {
            Log.d(TAG, "onCallMemberJoin user=$userInfo")
            val user = if (userInfo != null) JMUser.from(userInfo) else null
            notifyListener(Method_onCallMemberJoin, user = user)
        }

        override fun onCallDisconnected(reason: JMRtcClient.DisconnectReason) {
            Log.d(TAG, "onCallDisconnected reason=$reason")
            isConnected = false
            isConnecting = false
            //向服务器发送结束通话请求
            endCall()
            notifyListener(Method_onCallDisconnected, reason = reasonToCode(reason))
        }

        override fun onCallError(errorCode: Int, desc: String) {
            Log.d(TAG, "onCallError errorCode=$errorCode  desc=$desc")
            isConnected = false
            isConnecting = false
            if (errorCode == 872004 || errorCode == 872106) {
                JMRtcClient.getInstance().initEngine(mJMRtcListenerImpl)
            }
            // 872002 time out
            notifyListener(Method_onCallError, errorCode = errorCode, errorDesc = desc)
        }

        override fun onCallInviteReceived(session: JMRtcSession) {
            Log.d(TAG, "onCallInviteReceived session=$session")
            isConnecting = true
            JMRtcClient.getInstance().session.getInviterUserInfo(object : RequestCallback<UserInfo>() {
                override fun gotResult(responseCode: Int, responseMessage: String?, result: UserInfo?) {
                    val user = if (result != null) JMUser.from(result) else null
                    Log.d(TAG, "onCallInviteReceived user=$user")
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
        val size = mCallStateListeners.size
        for (i in 0 until size) {
            try {
                when (methodId) {
                    Method_onCallOutgoing -> mCallStateListeners[i].onCallOutgoing()
                    Method_onCallInviteReceived -> mCallStateListeners[i].onCallInviteReceived(user)
                    Method_onCallOtherUserInvited -> mCallStateListeners[i].onCallOtherUserInvited(user)
                    Method_onCallConnected -> mCallStateListeners[i].onCallConnected()
                    Method_onCallMemberJoin -> mCallStateListeners[i].onCallMemberJoin(user)
                    Method_onCallMemberOffline -> mCallStateListeners[i].onCallMemberOffline(user, reason)
                    Method_onCallDisconnected -> mCallStateListeners[i].onCallDisconnected(reason)
                    Method_onCallError -> mCallStateListeners[i].onCallError(errorCode, errorDesc)
                }
            } catch (e: RemoteException) {
                Log.e(TAG, "error:${e.message}")
            }
        }
    }

    private fun wakeUp() {
        // 获取电源管理器对象
        val pm = application.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isInteractive = pm.isInteractive
        if (!isInteractive) {
            @Suppress("DEPRECATION")
            val wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    or PowerManager.ACQUIRE_CAUSES_WAKEUP, "Phoney:VOICE_CALL")
            wl.acquire(10000)
            wl.release()
        }
    }

    fun startVoiceCallActivity(user: JMUser?) {
        wakeUp()
        //NotificationHelper.showNotification(this)
        val intent = Intent(application, VoiceAnswerActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            user?.let { putExtra(Constants.Extra.CALLER, it) }
        }
        application.startActivity(intent)
    }

    fun hangup() {
        reset()
        JMRtcClient.getInstance().hangup(object : BasicCallback() {
            override fun gotResult(code: Int, message: String?) {
                Log.d(TAG, "hangup==========> code=$code  message=$message")
            }
        })
    }

    private fun reset() {
        isMute = false
        isSpeakerEnabled = false
        mHandler.removeCallbacksAndMessages(null)
        //mCallee 不等于 null 说明是主叫方
        if (mCallee != null) {
            if (isConnected) {
                endCall()
            } else {
                closeWebSocket()
            }
            //挂断后 设置 mCallee 置为 null
            mCaller = null
            mCallee = null
        }
    }

    private var mCaller: JMUser? = null
    private var mCallee: JMUser? = null

    fun call(caller: JMUser, callee: JMUser) {
        mCaller = caller
        mCallee = callee
        connect(caller.token)
    }

    private fun startJMCall() {
        mCallee?.username?.let { username ->
            //JMRtcClient.getInstance().initEngine(mJMRtcListenerImpl)
            JMessageClient.getUserInfo(username, object : GetUserInfoCallback() {
                override fun gotResult(responseCode: Int, responseMessage: String?, user: UserInfo?) {
                    user?.let { u ->
                        JMRtcClient.getInstance().call(listOf(u), JMSignalingMessage.MediaType.AUDIO, object : BasicCallback() {
                            override fun gotResult(code: Int, message: String?) {
                                Log.d(TAG, "call============> code=$code  message=$message")
                                if (code == 872001) {
                                    retryInitEngineAndCall()
                                }
                            }
                        })
                    }
                }
            })
        }
    }

    private fun retryInitEngineAndCall() {
        JMRtcClient.getInstance().initEngine(mJMRtcListenerImpl)
        mHandler.postDelayed({
            startJMCall()
        }, 1000)
    }

    fun accept() {
        JMRtcClient.getInstance().accept(object : BasicCallback() {
            override fun gotResult(code: Int, message: String?) {
                Log.d(TAG, "accept==========> code=$code  message=$message")
            }
        })
    }

    fun refuse() {
        JMRtcClient.getInstance().refuse(object : BasicCallback() {
            override fun gotResult(code: Int, message: String?) {
                Log.d(TAG, "refuse==========> code=$code  message=$message")
            }
        })
    }

    fun switchSpeaker(): Boolean {
        isSpeakerEnabled = !isSpeakerEnabled
        JMRtcClient.getInstance().enableSpeakerphone(isSpeakerEnabled)
        return isSpeakerEnabled
    }

    fun switchMute(): Boolean {
        isMute = !isMute
        JMRtcClient.getInstance().enableAudio(!isMute)
        return isMute
    }

    fun isConnected(): Boolean {
        return this.isConnected
    }

    fun isConnecting(): Boolean {
        return this.isConnecting
    }

    fun registerCallStateListener(listener: CallStateListener) {
        mCallStateListeners.add(listener)
    }

    fun unregisterCallStateListener(listener: CallStateListener) {
        mHandler.removeCallbacksAndMessages(null)
        mCallStateListeners.remove(listener)
    }

    private fun connect(token: String) {
        Log.d("VOICE_CALL", "=====================================================")
        Log.d("VOICE_CALL", "start connecting----------:")
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
        val url = buildUrl(token)
        val request = Request.Builder().url(url).build()
        client.newWebSocket(request, mVoiceCallWebSocketListener)
        client.dispatcher().executorService().shutdown()
    }

    private fun buildUrl(token: String): String {
        return "${VoiceCallEngine.URL}?token=$token"
    }


    private var mState = 1
    private var mCallUuid: String? = null

    private fun checkSate(response: String) {
        when (mState) {
            STATE_ASK_CALL -> {
                Log.d("VOICE_CALL", "check state-[ask call]----: $response")
                val res = response.split(":")
                if (res.size == 2) {
                    if (RESPONSE_ASK_CALL_MSG == res[0]) {
                        mCallUuid = res[1]
                        //发起极光语音请求
                        startJMCall()
                        return
                    }
                }
                if (RESPONSE_NO_BALANCE_MSG == response || RESPONSE_NO_BALANCE_MSG1 == response) {
                    notifyListener(Method_onCallError, errorCode = ERROR_NO_BALANCE)
                } else {
                    notifyListener(Method_onCallError, errorCode = ERROR_REQUEST_FAIL)
                }
            }
            STATE_START_CALL -> {
                Log.d("VOICE_CALL", "check state-[start call]--: $response")
                if (RESPONSE_START_CALL_MSG == response) {
                    Log.d("VOICE_CALL", "开始检查状态")
                    //正常开始通话
                    checkCall()
                    return
                } else if (RESPONSE_NO_BALANCE_MSG == response || RESPONSE_NO_BALANCE_MSG1 == response) {
                    //余额不足
                    notifyListener(Method_onCallError, errorCode = ERROR_NO_BALANCE)
                }
            }
            STATE_CHECK_CALL -> {
                Log.d("VOICE_CALL", "check state-[check call]--: $response")
                if (RESPONSE_CHECK_CALL_OK_MSG == response || "succ" == response) {
                    Log.d("VOICE_CALL", "30秒后再次检查状态")
                    mHandler.postDelayed({
                        checkCall()
                    }, 30000)
                    return
                } else if (RESPONSE_NO_BALANCE_MSG == response || RESPONSE_NO_BALANCE_MSG1 == response) {
                    //余额不足
                    mHandler.postDelayed({
                        //余额不足时5秒后断开通话
                        if (isConnected) {
                            Log.d("VOICE_CALL", "余额不足，5秒后挂机")
                            hangup()
                        }
                    }, 5000)
                    notifyListener(Method_onCallError, errorCode = ERROR_NO_BALANCE)
                }
            }
            STATE_ENDING_CALL -> {
                Log.d("VOICE_CALL", "check state-[ending call]-: $response")
                closeWebSocket()
                if (RESPONSE_ENDING_CALL_MSG == response) {
                    return
                }
            }
            STATE_ABORT_CALL -> {
                Log.d("VOICE_CALL", "check state-[abort call]--: $response")
                closeWebSocket()
                if (RESPONSE_ABORT_CALL_MSG == response) {
                    return
                }

            }
        }
    }

    private fun askCall() {
        mHandler.removeCallbacksAndMessages(null)
        val uid = mCaller?.uuid ?: return
        mState = STATE_ASK_CALL
        val msg = "$REQUEST_ASK_CALL_MSG:$uid"
        Log.d("VOICE_CALL", "=====================================================")
        Log.d("VOICE_CALL", "ask call send-------------: $msg")
        mWebSocket?.send(msg)
    }

    private fun startCall() {
        mHandler.removeCallbacksAndMessages(null)
        mState = STATE_START_CALL
        val msg = "$REQUEST_START_CALL_MSG:$mCallUuid"
        Log.d("VOICE_CALL", "=====================================================")
        Log.d("VOICE_CALL", "start call send-----------: $msg")
        mWebSocket?.send(msg)
    }

    private fun checkCall() {
        mState = STATE_CHECK_CALL
        val msg = "$REQUEST_CHECK_CALL_MSG:$mCallUuid"
        Log.d("VOICE_CALL", "=====================================================")
        Log.d("VOICE_CALL", "check call send-----------: $msg")
        mWebSocket?.send(msg)
    }

    private fun endCall() {
        mHandler.removeCallbacksAndMessages(null)
        mState = STATE_ENDING_CALL
        val msg = "$REQUEST_ENDING_CALL_MSG:$mCallUuid"
        Log.d("VOICE_CALL", "=====================================================")
        Log.d("VOICE_CALL", "ending call send----------: $msg")
        mWebSocket?.send(msg)
    }

    @Suppress("unused")
    private fun abortCall() {
        mHandler.removeCallbacksAndMessages(null)
        mState = STATE_ABORT_CALL
        val msg = "$REQUEST_ABORT_CALL_MSG:$mCallUuid"
        Log.d("VOICE_CALL", "=====================================================")
        Log.d("VOICE_CALL", "abort call send-----------: $msg")
        mWebSocket?.send(msg)
    }

    private fun closeWebSocket() {
        mWebSocket?.close(1000, "bye")
    }

    inner class VoiceCallWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "onOpen============>$response")
            mWebSocket = webSocket
            Log.d("VOICE_CALL", "state-[socket opened]-----:")
            askCall()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "onMessage 1=======>$text")
            printLog(text)
            checkSate(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d(TAG, "onMessage 2=======>$bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "onClosing=========>code=$code   reason=$reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "onClosed==========>code=$code   reason=$reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d(TAG, "onFailure=========>t=$t   response=$response")
        }

        private fun printLog(text: String) {
            var eq: String? = null
            when {
                text.contains(RESPONSE_ASK_CALL_MSG) -> eq = "RESPONSE_ASK_CALL_MSG"
                text.contains(RESPONSE_START_CALL_MSG) -> eq = "RESPONSE_START_CALL_MSG"
                text.contains(RESPONSE_CHECK_CALL_OK_MSG) -> eq = "RESPONSE_CHECK_CALL_OK_MSG"
                text.contains(RESPONSE_NO_BALANCE_MSG) -> eq = "RESPONSE_NO_BALANCE_MSG"
                text.contains(RESPONSE_NO_BALANCE_MSG1) -> eq = "RESPONSE_NO_BALANCE_MSG1"
                text.contains(RESPONSE_ENDING_CALL_MSG) -> eq = "RESPONSE_ENDING_CALL_MSG"
                text.contains(RESPONSE_ABORT_CALL_MSG) -> eq = "RESPONSE_ABORT_CALL_MSG"
                text.contains(RESPONSE_HEART_MSG) -> eq = "RESPONSE_HEART_MSG"
            }
            Log.d("VOICE_CALL", "response value------------: $text == $eq")
            //Log.d("VOICE_CALL", "RESPONSE_ASK_CALL_MSG-----: $RESPONSE_ASK_CALL_MSG")
            //Log.d("VOICE_CALL", "RESPONSE_START_CALL_MSG---: $RESPONSE_START_CALL_MSG")
            //Log.d("VOICE_CALL", "RESPONSE_CHECK_CALL_OK_MSG: $RESPONSE_CHECK_CALL_OK_MSG")
            //Log.d("VOICE_CALL", "RESPONSE_NO_BALANCE_MSG---: $RESPONSE_NO_BALANCE_MSG")
            //Log.d("VOICE_CALL", "RESPONSE_ENDING_CALL_MSG--: $RESPONSE_ENDING_CALL_MSG")
            //Log.d("VOICE_CALL", "RESPONSE_ABORT_CALL_MSG---: $RESPONSE_ABORT_CALL_MSG")
            //Log.d("VOICE_CALL", "RESPONSE_HEART_MSG--------: $RESPONSE_HEART_MSG")
        }

    }

    interface CallStateListener {
        fun onCallOutgoing()

        fun onCallInviteReceived(user: JMUser?)

        fun onCallOtherUserInvited(user: JMUser?)

        fun onCallConnected()

        fun onCallMemberJoin(user: JMUser?)

        fun onCallMemberOffline(user: JMUser?, reason: Int)

        fun onCallDisconnected(reason: Int)

        fun onCallError(errorCode: Int, errorDesc: String?)
    }
}