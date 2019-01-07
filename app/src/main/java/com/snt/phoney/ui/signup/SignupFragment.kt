package com.snt.phoney.ui.signup

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.SigninFragmentBinding
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.ui.password.ForgetPasswordFragment
import com.snt.phoney.ui.setup.SetupWizardActivity
import kotlinx.android.synthetic.main.fragment_signin.*

/**
 */
class SignupFragment : BaseFragment() {

    private lateinit var viewModel: SignupViewModel
    var binding by autoCleared<SigninFragmentBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignupViewModel::class.java)
        binding.viewModel = viewModel

        login.setOnClickListener {
            viewModel.signup(phone.text.toString(), verificationCode.text.toString())
        }
        getVerificationCode.setOnClickListener {
            val phoneNumber = getPhone()
            if (!TextUtils.isEmpty(phoneNumber)) {
                viewModel.requestVerificationCode(phoneNumber!!)
            }
        }
        forgetPassword.setOnClickListener { onForgetPasswordClicked() }

        viewModel.verificationCode.observe(this, Observer {
            startCountdown()
            snackbar(getString(R.string.verification_code_has_send))
        })

        viewModel.user.observe(this, Observer { user ->
            snackbar("登录成功")
            //context?.let { context -> startActivity(SetupWizardActivity.newIntent(context, user)) }
            if (user.validated) {
                context?.let { context -> startActivity(MainActivity.newIntent(context)) }
            } else {
                context?.let { context -> startActivity(SetupWizardActivity.newIntent(context, user)) }
            }
            activity?.finish()
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        super.onDestroyView()
    }

    private var countDownTimer: CountDownTimer? = null

    private fun startCountdown() {
        getVerificationCode.isEnabled = false
        getVerificationCode.text = "60${getString(R.string.unit_second)}"
        countDownTimer = object : CountDownTimer(60000, 1000) {
            private var tick = 60
            override fun onFinish() {
                getVerificationCode.setText(R.string.get_verification_code)
                getVerificationCode.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                if (tick > 0) {
                    getVerificationCode.text = "${(tick--)}${getString(R.string.unit_second)}"
                }
            }
        }
        countDownTimer?.start()
    }

    private fun getPhone(): String? {
        if (TextUtils.isEmpty(phone.text)) {
            snackbar(getString(R.string.please_input_phone_number))
            return null
        } else {
            return phone.text.toString()
        }
    }

    private fun onForgetPasswordClicked() {
        activity?.addFragmentSafely(R.id.containerLayout, ForgetPasswordFragment.newInstance(), "forget_password", true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignupFragment()
    }
}
