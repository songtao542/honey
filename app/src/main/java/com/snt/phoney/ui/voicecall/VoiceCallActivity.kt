package com.snt.phoney.ui.voicecall

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.snt.phoney.ICallStateListener
import com.snt.phoney.IVoiceCallService
import com.snt.phoney.R
import com.snt.phoney.base.BaseNoViewModelActivity
import com.snt.phoney.domain.model.ImUser
import com.snt.phoney.domain.model.JMUser
import com.snt.phoney.extensions.checkAndRequestPermission
import com.snt.phoney.extensions.checkAppPermission
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.service.*
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.activity_voice_call.*


class VoiceCallActivity : BaseNoViewModelActivity(), ServiceConnection {

    companion object {
        @JvmStatic
        fun start(context: Context, im: ImUser) {
            val intent = Intent(context, VoiceCallActivity::class.java).apply {
                putExtra(Constants.Extra.USER, JMUser.from(im))
            }
            context.startActivity(intent)
        }
    }

    private val mHandler = Handler()
    private var mVoiceCallService: IVoiceCallService? = null
    private var mICallStateListener: ICallStateListenerImpl? = null


    private var mUser: JMUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()

        mUser = intent?.getParcelableExtra(Constants.Extra.USER)
        if (mUser == null) {
            finish()
        }

        setContentView(R.layout.activity_voice_call)

        setupUser()

        if (checkAndRequestPermission(*getPermissions())) {
            bindService(Intent(this, VoiceCallService::class.java), this, Context.BIND_AUTO_CREATE)
        }

        cancelAndHangup.setOnClickListener {
            mVoiceCallService?.hangup()
            finish()
        }

        call.setOnClickListener {
            state.text = getString(R.string.wait_accept_template, mUser?.nickname
                    ?: getString(R.string.other_side))
            setupCancelUI()
            mVoiceCallService?.call(mUser)
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
        if (mVoiceCallService?.isConnected == true ||
                mVoiceCallService?.isConnecting == true) {
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
        if (mVoiceCallService?.isConnected == false) {
            mVoiceCallService?.call(mUser)
        }
    }

    private fun setupUser() {
        name.text = mUser?.nickname
        state.text = getString(R.string.wait_accept_template, mUser?.nickname
                ?: getString(R.string.other_side))
        Glide.with(this).load(mUser?.avatar)
                //.apply(RequestOptions().circleCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(head)
    }

    private fun setupCallUI() {
        cancelAndHangupLayout.visibility = View.GONE
        callLayout.visibility = View.VISIBLE
    }

    private fun setupCancelUI() {
        cancelAndHangupLabel.setText(R.string.cancel)
        cancelAndHangupLayout.visibility = View.VISIBLE
        callLayout.visibility = View.GONE
    }

    private fun setupHangupUI() {
        cancelAndHangupLabel.setText(R.string.hangup_phone)
        cancelAndHangupLayout.visibility = View.VISIBLE
        callLayout.visibility = View.GONE
    }

    private fun hangupCountDown() {
        val countDownTimer = object : CountDownTimer(6000, 1000) {
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
        countDownTimer.start()
    }

    inner class ICallStateListenerImpl : ICallStateListener.Stub() {
        override fun onCallOutgoing() {
            mHandler.post {
                state.text = getString(R.string.wait_accept_template, mUser?.nickname
                        ?: getString(R.string.other_side))
            }
        }

        override fun onCallInviteReceived(user: JMUser) {
        }

        override fun onCallOtherUserInvited(user: JMUser) {
        }

        override fun onCallConnected() {
            mHandler.post {
                state.setText(R.string.has_accept_phone)
                setupHangupUI()
            }
        }

        override fun onCallMemberJoin(user: JMUser) {
        }

        override fun onCallMemberOffline(user: JMUser, reason: Int) {
            mHandler.post {
                if (user.username == mUser?.username) {
                    if (reason == REASON_REFUSE) {
                        state.setText(R.string.has_refuse_phone)
                    } else if (reason == REASON_OFFLINE || reason == REASON_HANGUP) {
                        state.setText(R.string.has_hangup_phone)
                    } else if (reason == REASON_CANCEL) {
                        state.setText(R.string.has_no_answer)
                    } else if (reason == REASON_BUSY) {
                        state.setText(R.string.the_user_is_busy)
                    }
                    mVoiceCallService?.hangup()
                    setupCallUI()
                }
            }
        }

        override fun onCallDisconnected(reason: Int) {
            mHandler.post {
                if (reason == REASON_REFUSE) {
                    state.setText(R.string.has_refuse_phone)
                    setupCallUI()
                } else if (reason == REASON_HANGUP) {
                    setupCallUI()
                } else {

                }
            }
        }

        override fun onCallError(errorCode: Int, errorDesc: String?) {
            mHandler.post {
                if (errorCode == ERROR_TIMEOUT) {
                    state.setText(R.string.has_timeout)
                    setupCallUI()
                } else if (errorCode == ERROR_NO_BALANCE) {
                    hangupCountDown()
                } else {
                    finish()
                }
            }
        }
    }
}
