package com.snt.phoney.ui.voicecall

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AlertDialog
import com.snt.phoney.R
import com.snt.phoney.base.BaseNoViewModelActivity
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.VoiceCallService

class VoiceCallActivity : BaseNoViewModelActivity(), ServiceConnection {

    private var mService: VoiceCallService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()
        setContentView(R.layout.activity_voice_call)

        if (checkAndRequestPermission(Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
            bindService(Intent(this, VoiceCallService::class.java), this, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (checkAppPermission(Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS)) {
            bindService(Intent(this, VoiceCallService::class.java), this, Context.BIND_AUTO_CREATE)
        } else {
            finish()
        }
    }


    override fun onBackPressed() {
        if (mService?.isConnected() == true) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.cancel_voice_call_tip)
                    .setMessage(R.string.cancel_voice_call_warn)
                    .setPositiveButton(R.string.disconnect) { dialog, _ ->
                        dialog.dismiss()
                        disconnect()
                        finish()
                    }.show()
        } else {
            super.onBackPressed()
        }
    }

    private fun disconnect() {
        mService?.disconnect()
        unbindService(this)
    }


    override fun onServiceDisconnected(name: ComponentName?) {
        mService = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mService = (service as VoiceCallService.VoiceCallBinder).getService()
    }
}
