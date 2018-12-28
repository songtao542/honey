package com.snt.phoney.ui.voicecall

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.snt.phoney.ICallStateListener
import com.snt.phoney.IVoiceCallService
import com.snt.phoney.R
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.VoiceCallService
import kotlinx.android.synthetic.main.activity_voice_answer.*

class VoiceAnswerActivity : AppCompatActivity(), ServiceConnection {

    private var mVoiceCallService: IVoiceCallService? = null

    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()
        setContentView(R.layout.activity_voice_answer)
        if (checkAndRequestPermission(*getPermissions())) {
            bindService(Intent(this, VoiceCallService::class.java), this, Context.BIND_AUTO_CREATE)
        }

        hangup.setOnClickListener {
            mVoiceCallService?.hangup()
            finish()
        }

        accept.setOnClickListener {
            mVoiceCallService?.accept()
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
        }

        override fun onCallInviteReceived() {
            mHandler.post {
                state.setText(R.string.being_invited)
            }
        }

        override fun onCallOtherUserInvited() {
        }

        override fun onCallConnected() {
            mHandler.post {
                acceptLayout.visibility = View.GONE
                state.setText(R.string.has_accept_phone)
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
