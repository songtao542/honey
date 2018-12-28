package com.snt.phoney.ui.voicecall

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.domain.model.ImUser
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.MessageEvent
import com.snt.phoney.service.VoiceCallService
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.activity_voice_call.*


class VoiceCallActivity : AppCompatActivity(), ServiceConnection, Handler.Callback {

    companion object {
        @JvmStatic
        fun start(context: Context, im: ImUser) {
            val intent = Intent(context, VoiceCallActivity::class.java).apply {
                putExtra(Constants.Extra.USER, im)
            }
            context.startActivity(intent)
        }
    }

    private var mRemoteMessenger: Messenger? = null

    private val mMessenger = Messenger(Handler(this))

    override fun handleMessage(msg: Message?): Boolean {
        msg?.let { message ->
            when (message.what) {
                MessageEvent.EVENT_STATE -> {
                    val state = message.obj as Int
                    Log.d("TTTT", "sssssssssssssssssssssssssss state=$state")
                    if (state == MessageEvent.STATE_NOT_CONNECTED) {
                        hangupLabel.setText(R.string.cancel)
                        user?.username?.let { username ->
                            sendMessage(MessageEvent.CMD_CALL, username)
                        }
                    } else {
                        hangupLabel.setText(R.string.hangup)
                    }
                    return true
                }
                MessageEvent.EVENT_CONNECTING -> {
                    state.setText(R.string.connecting)
                }
                MessageEvent.EVENT_CONNECTED -> {
                    state.setText(R.string.has_accept_phone)
                    hangupLabel.setText(R.string.hangup)
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

    private var user: ImUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()

        user = intent?.getParcelableExtra(Constants.Extra.USER)
        if (user == null) {
            finish()
        }

        setContentView(R.layout.activity_voice_call)

        name.text = user?.nickname
        Glide.with(this).load(user?.avatar).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)

        if (checkAndRequestPermission(*getPermissions())) {
            bindService(Intent(this, VoiceCallService::class.java), this, Context.BIND_AUTO_CREATE)
        }

        hangup.setOnClickListener {
            hangupIfConnected()
            finish()
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
            reply.replyTo = mMessenger
            obj?.let { reply.obj = it }
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
