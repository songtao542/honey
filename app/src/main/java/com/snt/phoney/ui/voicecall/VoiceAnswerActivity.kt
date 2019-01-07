package com.snt.phoney.ui.voicecall

import android.Manifest
import android.app.KeyguardManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.model.UserInfo
import com.snt.phoney.ICallStateListener
import com.snt.phoney.IVoiceCallService
import com.snt.phoney.R
import com.snt.phoney.base.BaseNoViewModelActivity
import com.snt.phoney.domain.model.JMUser
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.VoiceCallService
import kotlinx.android.synthetic.main.activity_voice_answer.*


class VoiceAnswerActivity : BaseNoViewModelActivity(), ServiceConnection {

    private var mVoiceCallService: IVoiceCallService? = null
    private var mICallStateListener: ICallStateListenerImpl? = null

    private val mHandler = Handler()

    private var mInvitingUser: JMUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()

        //Api26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val keyGuardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyGuardManager.requestDismissKeyguard(this, object : KeyguardManager.KeyguardDismissCallback() {
                override fun onDismissError() {
                }

                override fun onDismissSucceeded() {
                }

                override fun onDismissCancelled() {
                }
            })
        }
        //api27
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        setContentView(R.layout.activity_voice_answer)
        if (checkAndRequestPermission(*getPermissions())) {
            bindService(Intent(this, VoiceCallService::class.java), this, Context.BIND_AUTO_CREATE)
        }

        mInvitingUser = intent.getParcelableExtra("user")
        setupUser()

        setupRefuseUI()

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
        if (mVoiceCallService?.isConnected == true) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.cancel_voice_call_tip)
                    .setMessage(R.string.cancel_voice_call_warn)
                    .setPositiveButton(R.string.disconnect) { dialog, _ ->
                        dialog.dismiss()
                        mVoiceCallService?.hangup()
                        finish()
                    }.show()
        } else if (mVoiceCallService?.isConnecting == true) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.reject_voice_call_tip)
                    .setMessage(R.string.reject_voice_call_warn)
                    .setPositiveButton(R.string.disconnect) { dialog, _ ->
                        dialog.dismiss()
                        mVoiceCallService?.refuse()
                        finish()
                    }.show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        mVoiceCallService?.unregisterICallStateListener(mICallStateListener)
        unbindService(this)
        super.onDestroy()
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mVoiceCallService = null
        mICallStateListener = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mVoiceCallService = IVoiceCallService.Stub.asInterface(service)
        mICallStateListener = ICallStateListenerImpl()
        mVoiceCallService?.registerICallStateListener(mICallStateListener)
    }

    private fun setupUser() {
        mInvitingUser?.let { user ->
            name.text = mInvitingUser?.nickname
            JMessageClient.getUserInfo(user.username, object : GetUserInfoCallback() {
                override fun gotResult(responseCode: Int, responseMessage: String?, userInfo: UserInfo?) {
                    userInfo?.let { userInfo ->
                        userInfo.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                            override fun gotResult(code: Int, message: String?, bitmap: Bitmap?) {
                                bitmap?.let { bitmap ->
                                    head.setImageBitmap(bitmap)
                                }
                            }
                        })
                    }
                }
            })
        }
    }

    private fun setupRefuseUI() {
        refuseAndHangup.setOnClickListener {
            mVoiceCallService?.refuse()
            finish()
        }
    }

    private fun setupHangupUI() {
        refuseAndHangup.setOnClickListener {
            mVoiceCallService?.hangup()
            finish()
        }
    }

    inner class ICallStateListenerImpl : ICallStateListener.Stub() {
        override fun onCallOutgoing() {
        }

        override fun onCallInviteReceived(user: JMUser) {
            mInvitingUser = user
            mHandler.post {
                setupUser()
                setupRefuseUI()
                state.setText(R.string.being_invited)
            }
        }

        override fun onCallOtherUserInvited(user: JMUser) {
        }

        override fun onCallConnected() {
            mHandler.post {
                acceptLayout.visibility = View.GONE
                state.setText(R.string.has_accept_phone)
                setupHangupUI()
            }
        }

        override fun onCallMemberJoin(user: JMUser) {
        }

        override fun onCallMemberOffline(user: JMUser, reason: Int) {
            mHandler.post {
                if (user.username == mInvitingUser?.username) {
                    mVoiceCallService?.hangup()
                    finish()
                }
            }
        }

        override fun onCallDisconnected(reason: Int) {
            mHandler.post {
                finish()
            }
        }

        override fun onCallError(errorCode: Int, errorDesc: String?) {
            mHandler.post {
                finish()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        //NotificationHelper.showNotification(this)
    }

    override fun onResume() {
        super.onResume()
        //NotificationHelper.cancelNotification(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

    }


}
