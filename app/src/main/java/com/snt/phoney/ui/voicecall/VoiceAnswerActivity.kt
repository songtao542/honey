package com.snt.phoney.ui.voicecall

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.snt.phoney.R
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.MessageEvent
import com.snt.phoney.service.VoiceCallService
import kotlinx.android.synthetic.main.activity_voice_answer.*

class VoiceAnswerActivity : AppCompatActivity(), ServiceConnection, Handler.Callback {

    private var mRemoteMessenger: Messenger? = null
    private val mMessenger = Messenger(Handler(this))

    override fun handleMessage(msg: Message?): Boolean {
        msg?.let { message ->
            when (message.what) {
                MessageEvent.EVENT_STATE -> {
                    val state = message.obj as Int
                    Log.d("TTTT", "aaaaa sssssssss state=$state")
                    if (state == MessageEvent.STATE_NOT_CONNECTED) {
                        acceptLayout.visibility = View.VISIBLE
                    } else {
                        acceptLayout.visibility = View.GONE
                    }
                    return true
                }
                MessageEvent.EVENT_CONNECTING -> {
                    state.setText(R.string.being_invited)
                }
                MessageEvent.EVENT_CONNECTED -> {
                    state.setText(R.string.has_accept_phone)
                    acceptLayout.visibility = View.GONE
                }
                MessageEvent.EVENT_DISCONNECTED -> {
                    /**
                     * 断开后直接关闭
                     */
                    finish()
                    return true
                }
                else -> {
                    return false
                }
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()
        setContentView(R.layout.activity_voice_answer)
        if (checkAndRequestPermission(*getPermissions())) {
            bindService(Intent(this, VoiceCallService::class.java), this, Context.BIND_AUTO_CREATE)
        }

        hangup.setOnClickListener {
            hangupIfConnected()
            finish()
        }

        accept.setOnClickListener {
            sendMessage(MessageEvent.CMD_ACCEPT)
            acceptLayout.visibility = View.GONE
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkAppPermission(*getPermissions())) {
            bindService(Intent(this, VoiceCallService::class.java), this, Context.BIND_AUTO_CREATE)
        } else {
            finish()
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS)
    }


    override fun onBackPressed() {
        if (mRemoteMessenger != null) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.cancel_voice_call_tip)
                    .setMessage(R.string.cancel_voice_call_warn)
                    .setPositiveButton(R.string.disconnect) { dialog, _ ->
                        dialog.dismiss()
                        hangupIfConnected()
                        finish()
                    }.show()
        } else {
            super.onBackPressed()
        }
    }

    private fun hangupIfConnected() {
        mRemoteMessenger?.let { messenger ->
            val message = Message.obtain()
            message.what = MessageEvent.CMD_HANGUP
            messenger.send(message)
            return@let
        }
    }

    private fun sendMessage(what: Int, obj: Any? = null) {
        mRemoteMessenger?.let { messenger ->
            val reply = Message.obtain()
            reply.what = what
            obj?.let { reply.obj = it }
            reply.replyTo = mMessenger
            messenger.send(reply)
            return@let
        }
    }

    override fun onDestroy() {
        unbindService(this)
        super.onDestroy()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mRemoteMessenger = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mRemoteMessenger = Messenger(service)
        sendMessage(MessageEvent.CMD_QUERY_STATE)
    }
}
