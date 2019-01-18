package com.snt.phoney.ui.voicecall2

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.callback.GetUserInfoCallback
import cn.jpush.im.android.api.model.UserInfo
import com.snt.phoney.R
import com.snt.phoney.base.BaseNoViewModelActivity
import com.snt.phoney.domain.model.JMUser
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.circle
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.VoiceCallEngine
import com.snt.phoney.utils.data.Constants
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_voice_answer.*


class VoiceAnswerActivity : BaseNoViewModelActivity() {

    private var mCallStateListener: CallStateListenerImpl = CallStateListenerImpl()

    private val mHandler = Handler()

    private var mCaller: JMUser? = null

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

        checkAndRequestPermission(*getPermissions())

        VoiceCallEngine.getInstance().registerCallStateListener(mCallStateListener)

        mCaller = intent.getParcelableExtra(Constants.Extra.CALLER)
        setupUser()

        setupCallInUI()

        acceptButton.setOnClickListener {
            VoiceCallEngine.getInstance().accept()
            acceptButton.visibility = View.GONE
        }

        speakerButton.setOnClickListener {
            val enable = VoiceCallEngine.getInstance().switchSpeaker()
            speakerButton.isSelected = enable
        }

        muteButton.setOnClickListener {
            val enable = VoiceCallEngine.getInstance().switchMute()
            muteButton.isSelected = enable
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (!checkAppPermission(*getPermissions())) {
            finish()
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS)
    }


    override fun onBackPressed() {
        @Suppress("CascadeIf")
        if (VoiceCallEngine.getInstance().isConnected()) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.cancel_voice_call_tip)
                    .setMessage(R.string.cancel_voice_call_warn)
                    .setPositiveButton(R.string.disconnect) { dialog, _ ->
                        dialog.dismiss()
                        VoiceCallEngine.getInstance().hangup()
                        finish()
                    }.show()
        } else if (VoiceCallEngine.getInstance().isConnecting()) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.reject_voice_call_tip)
                    .setMessage(R.string.reject_voice_call_warn)
                    .setPositiveButton(R.string.disconnect) { dialog, _ ->
                        dialog.dismiss()
                        VoiceCallEngine.getInstance().refuse()
                        finish()
                    }.show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        VoiceCallEngine.getInstance().unregisterCallStateListener(mCallStateListener)
        super.onDestroy()
    }

    private fun setupUser() {
        mCaller?.let { user ->
            name.text = mCaller?.nickname
            JMessageClient.getUserInfo(user.username, object : GetUserInfoCallback() {
                override fun gotResult(responseCode: Int, responseMessage: String?, userInfo: UserInfo?) {
                    userInfo?.let { user ->
                        user.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                            override fun gotResult(code: Int, message: String?, bitmap: Bitmap?) {
                                bitmap?.let { image ->
                                    head.setImageBitmap(image.circle())
                                }
                            }
                        })
                    }
                }
            })
        }
    }

    /**
     * 被呼叫状态
     */
    private fun setupCallInUI() {
        state.setText(R.string.being_invited)
        //显示接听按钮
        acceptButton.visibility = View.VISIBLE
        //隐藏扬声器按钮
        speakerButton.visibility = View.GONE
        muteButton.visibility = View.GONE
        //此时 refuseAndHangup 按钮执行 refuse 功能
        refuseOrHangupButton.setOnClickListener {
            VoiceCallEngine.getInstance().refuse()
            finish()
        }
    }

    /**
     * 已连接状态
     */
    private fun setupConnectedUI() {
        state.setText(R.string.has_accept_phone)
        //隐藏接听按钮
        acceptButton.visibility = View.GONE
        //显示扬声器按钮
        speakerButton.visibility = View.VISIBLE
        muteButton.visibility = View.VISIBLE
        //此时 refuseAndHangup 按钮执行 hangup 功能
        refuseOrHangupButton.setOnClickListener {
            VoiceCallEngine.getInstance().hangup()
            finish()
        }
    }

    inner class CallStateListenerImpl : VoiceCallEngine.CallStateListener {
        override fun onCallOutgoing() {
        }

        override fun onCallInviteReceived(user: JMUser?) {
            mCaller = user
            mHandler.post {
                setupUser()
                setupCallInUI()
            }
        }

        override fun onCallOtherUserInvited(user: JMUser?) {
        }

        override fun onCallConnected() {
            mHandler.post {
                setupConnectedUI()
            }
        }

        override fun onCallMemberJoin(user: JMUser?) {
        }

        override fun onCallMemberOffline(user: JMUser?, reason: Int) {
            mHandler.post {
                if (user?.username == mCaller?.username) {
                    VoiceCallEngine.getInstance().hangup()
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
        MobclickAgent.onPageEnd(javaClass.simpleName)
        //NotificationHelper.showNotification(this)
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(javaClass.simpleName)
        //NotificationHelper.cancelNotification(this)
        @Suppress("DEPRECATION")
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
    }


}
