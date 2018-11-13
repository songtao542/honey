package com.snt.phoney.ui.signin

import android.os.Bundle
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
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.ui.password.ForgetPasswordFragment
import kotlinx.android.synthetic.main.fragment_signin.*

/**
 * A simple [Fragment] subclass.
 * Use the [SigninFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SigninFragment : BaseFragment() {

    private lateinit var viewModel: SigninViewModel
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SigninViewModel::class.java)
        binding.viewModel = viewModel

        login.setOnClickListener {
            viewModel.signup(phone.text.toString(), verificationCode.text.toString()).disposedBy(disposeBag)
        }
        getVerificationCode.setOnClickListener {
            viewModel.getVerificationCode(phone.text.toString()).disposedBy(disposeBag)
        }
        forgetPassword.setOnClickListener { onForgetPasswordClicked() }

        viewModel.verificationCode.observe(this, Observer {
            snackbar(getString(R.string.verification_code_has_send))
        })

        viewModel.user.observe(this, Observer {
            snackbar("注册成功")
            context?.let { startActivity(MainActivity.newIntent(it)) }
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

    }

    private fun onForgetPasswordClicked() {
        activity?.addFragmentSafely(R.id.containerLayout, ForgetPasswordFragment.newInstance(), "forget_password", true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SigninFragment()
    }
}
