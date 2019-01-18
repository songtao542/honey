package com.snt.phoney.ui.voicecall2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.BaseNoViewModelActivity
import com.snt.phoney.domain.model.JMUser
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.*
import com.snt.phoney.utils.data.Constants
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_voice_call.*


class VoiceCallActivity : BaseNoViewModelActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, caller: JMUser, callee: JMUser) {
            val intent = Intent(context, VoiceCallActivity::class.java).apply {
                putExtra(Constants.Extra.CALLER, caller)
                putExtra(Constants.Extra.CALLEE, callee)
            }
            context.startActivity(intent)
        }
    }

    private val mHandler = Handler()

    private var mCallStateListener: VoiceCallEngine.CallStateListener = CallStateListenerImpl()


    private var mCaller: JMUser? = null
    private var mCallee: JMUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()

        mCaller = intent?.getParcelableExtra(Constants.Extra.CALLER)
        mCallee = intent?.getParcelableExtra(Constants.Extra.CALLEE)
        if (mCaller == null || mCallee == null) {
            finish()
        }

        setContentView(R.layout.activity_voice_call)

        setupUser()

        VoiceCallEngine.getInstance().registerCallStateListener(mCallStateListener)

        if (checkAndRequestPermission(*getPermissions())) {
            VoiceCallEngine.getInstance().call(mCaller!!, mCallee!!)
        }

        cancelOrHangupButton.setOnClickListener {
            VoiceCallEngine.getInstance().hangup()
            finish()
        }

        callButton.setOnClickListener {
            state.text = getString(R.string.wait_accept_template, mCallee?.nickname
                    ?: getString(R.string.other_side))
            setupCancelUI()
            VoiceCallEngine.getInstance().call(mCaller!!, mCallee!!)
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
        if (checkAppPermission(*getPermissions())) {
            VoiceCallEngine.getInstance().call(mCaller!!, mCallee!!)
        } else {
            finish()
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS)
    }

    override fun onBackPressed() {
        if (VoiceCallEngine.getInstance().isConnected() || VoiceCallEngine.getInstance().isConnecting()) {
            AlertDialog.Builder(this)
                    .setTitle(R.string.cancel_voice_call_tip)
                    .setMessage(R.string.cancel_voice_call_warn)
                    .setPositiveButton(R.string.disconnect) { dialog, _ ->
                        dialog.dismiss()
                        VoiceCallEngine.getInstance().hangup()
                        finish()
                    }.show()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        VoiceCallEngine.getInstance().unregisterCallStateListener(mCallStateListener)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(javaClass.simpleName)
    }

    private fun setupUser() {
        name.text = mCallee?.nickname
        state.text = getString(R.string.wait_accept_template, mCallee?.nickname
                ?: getString(R.string.other_side))
        Glide.with(this).load(mCallee?.avatar)
                .apply(RequestOptions().circleCrop().placeholder(R.drawable.ic_head_placeholder).error(R.drawable.ic_head_placeholder))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(head)
    }

    /**
     * 对方拒绝或者挂断后，显示 可以呼叫 状态
     */
    private fun setupCallOutUI(reason: Int, hangupByMyself: Boolean = false) {
        if (reason == REASON_REFUSE) {
            state.setText(R.string.has_refuse_phone)
            mHandler.postDelayed({
                finish()
            }, 500)
            return
        } else if (reason == REASON_OFFLINE || reason == REASON_HANGUP) {
            if (hangupByMyself) {
                state.setText(R.string.has_hangup_phone)
            } else {
                state.setText(R.string.has_hangup_phone_by_other)
            }
            mHandler.postDelayed({
                finish()
            }, 500)
            return
        } else if (reason == REASON_CANCEL) {
            state.setText(R.string.has_no_answer)
        } else if (reason == REASON_BUSY) {
            state.setText(R.string.the_user_is_busy)
        } else if (reason == REASON_REFUSE) {
            state.setText(R.string.has_refuse_phone)
        } else if (reason == ERROR_TIMEOUT) {
            state.setText(R.string.has_timeout)
        }
        cancelOrHangupButton.visibility = View.GONE
        callButton.visibility = View.VISIBLE
        speakerButton.visibility = View.GONE
        muteButton.visibility = View.GONE
    }

    /**
     *  正在呼叫中 显示 cancel 状态
     */
    private fun setupCancelUI() {
        cancelOrHangupButton.setText(R.string.cancel)
        cancelOrHangupButton.visibility = View.VISIBLE
        callButton.visibility = View.GONE
        speakerButton.visibility = View.GONE
        muteButton.visibility = View.GONE
    }

    /**
     * 已接通后
     */
    private fun setupConnectedUI() {
        cancelOrHangupButton.setText(R.string.hangup_phone)
        cancelOrHangupButton.visibility = View.VISIBLE
        callButton.visibility = View.GONE
        speakerButton.visibility = View.VISIBLE
        muteButton.visibility = View.VISIBLE
    }

    private var countDownTimer: CountDownTimer? = null

    private fun hangupCountDown() {
        countDownTimer = object : CountDownTimer(6000, 1000) {
            var count = 5
            override fun onFinish() {
                finish()
            }

            override fun onTick(millisUntilFinished: Long) {
                if (count >= 0) {
                    state.text = getString(R.string.no_balance_template, (count--).toString())
                }
            }
        }
        countDownTimer?.start()
    }

    inner class CallStateListenerImpl : VoiceCallEngine.CallStateListener {
        override fun onCallOutgoing() {
            mHandler.post {
                state.text = getString(R.string.wait_accept_template, mCallee?.nickname
                        ?: getString(R.string.other_side))
            }
        }

        /**
         * 该页面是主叫方，所以不用处理该回调
         */
        override fun onCallInviteReceived(user: JMUser?) {
        }

        override fun onCallOtherUserInvited(user: JMUser?) {
        }

        override fun onCallConnected() {
            mHandler.post {
                state.setText(R.string.has_accept_phone)
                setupConnectedUI()
            }
        }

        override fun onCallMemberJoin(user: JMUser?) {
        }

        override fun onCallMemberOffline(user: JMUser?, reason: Int) {
            mHandler.post {
                if (user?.username == mCallee?.username) {
                    VoiceCallEngine.getInstance().hangup()
                    setupCallOutUI(reason)
                }
            }
        }

        override fun onCallDisconnected(reason: Int) {
            mHandler.post {
                setupCallOutUI(reason)
            }
        }

        override fun onCallError(errorCode: Int, errorDesc: String?) {
            mHandler.post {
                @Suppress("CascadeIf")
                if (errorCode == ERROR_TIMEOUT) {
                    setupCallOutUI(errorCode)
                } else if (errorCode == ERROR_NO_BALANCE) {
                    hangupCountDown()
                } else {
                    finish()
                }
            }
        }
    }
}
