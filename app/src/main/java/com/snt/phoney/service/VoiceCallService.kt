package com.snt.phoney.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.BuildConfig
import com.snt.phoney.domain.accessor.UserAccessor
import com.snt.phoney.utils.data.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class VoiceCallService : Service() {

    companion object {
        @JvmStatic
        private val URL = "ws://${Constants.Api.DOMAIN_NAME}/im/websocket"
    }

    private var mWebSocket: WebSocket? = null
    private var mVoiceCallWebSocketListener: VoiceCallWebSocketListener? = VoiceCallWebSocketListener()
    private val mBinder: VoiceCallBinder = VoiceCallBinder()

    private val mHandler: Handler = Handler()

    private val mState = MutableLiveData<State>()

    @Inject
    lateinit var mUserAccessor: UserAccessor

    override fun onBind(intent: Intent): IBinder {

        return mBinder
    }


    inner class VoiceCallBinder : Binder() {
        fun getService(): VoiceCallService {
            return this@VoiceCallService
        }
    }

    fun getState(): MutableLiveData<State> {
        return mState
    }

    fun disconnect() {

    }

    fun isConnected(): Boolean {
        return true
    }

    fun connectJM(){
        //JMRtcClient.getInstance().initEngine(JMRtcListener listener);
    }

    fun connect() {
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
