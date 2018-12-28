package com.snt.phoney.ui.voicecall

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.ICallStateListener
import com.snt.phoney.IVoiceCallService
import com.snt.phoney.R
import com.snt.phoney.domain.model.ImUser
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.VoiceCallService
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.activity_voice_call.*


class VoiceCallActivity : AppCompatActivity(), ServiceConnection {

    companion object {
        @JvmStatic
        fun start(context: Context, im: ImUser) {
            val intent = Intent(context, VoiceCallActivity::class.java).apply {
                putExtra(Constants.Extra.USER, im)
            }
            context.startActivity(intent)
        }
    }

    private val mHandler = Handler()
    private var mVoiceCallService: IVoiceCallService? = null


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
            mVoiceCallService?.hangup()
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
        if (mVoiceCallService != null) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.cancel_voice_call_tip)
                    .setMessage(R.string.cancel_voice_call_warn)
                    .setPositiveButton(R.string.disconnect) { dialog, _ ->
                        dialog.dismiss()
                        mVoiceCallService?.hangup()
                        finish()
                    }.show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        unbindService(this)
        super.onDestroy()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mVoiceCallService = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mVoiceCallService = IVoiceCallService.Stub.asInterface(service)
    }


    inner class ICallStateListenerImpl : ICallStateListener.Stub() {
        override fun onCallOutgoing() {
            mHandler.post {
                state.setText(R.string.connecting)
            }
        }

        override fun onCallInviteReceived() {
        }

        override fun onCallOtherUserInvited() {
        }

        override fun onCallConnected() {
            mHandler.post {
                state.setText(R.string.has_accept_phone)
                hangupLabel.setText(R.string.hangup)
            }
        }

        override fun onCallMemberJoin() {
        }

        override fun onCallMemberOffline() {
        }

        override fun onCallDisconnected() {
            mHandler.post {
                finish()
            }
        }

        override fun onCallError() {
            mHandler.post {
                finish()
            }
        }
    }
}
