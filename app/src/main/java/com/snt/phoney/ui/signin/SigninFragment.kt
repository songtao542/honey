package com.snt.phoney.ui.signin

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.SigninFragmentBinding

import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.dating.create.CreateDatingActivity
import com.snt.phoney.ui.nearby.NearbyActivity
import com.snt.phoney.ui.signup.SignupActivity

/**
 * A simple [Fragment] subclass.
 * Use the [SigninFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SigninFragment : BaseFragment() {

    lateinit var viewModel: SigninViewModel
    var binding by autoCleared<SigninFragmentBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signin, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SigninViewModel::class.java)
        binding.loginViewModel = viewModel

        binding.login.setOnClickListener { onLoginButtonClicked() }
        binding.forgetPassword.setOnClickListener { onForgetPasswordClicked() }
        binding.qq.setOnClickListener { onQQClicked() }
        binding.weixin.setOnClickListener { onWeixinClicked() }
        binding.weibo.setOnClickListener { onWeiboClicked() }

    }


    private fun onLoginButtonClicked() {
        Log.d("TTTT", "click login " + viewModel)
        viewModel.signin("songtao", "wangsongtao").subscribe { response: Response<User>? ->
            Log.d("TTTT", "response:" + response?.data)
            viewModel.updateUser(response?.data)
        }.disposedBy(disposeBag)

//        viewModel.login("songtao", "wangsongtao").observe(this, Observer {
//            Log.d("TTTT", "data:" + it?.data)
//        })
    }

    private fun onForgetPasswordClicked() {

    }

    private fun onWeiboClicked() {
        context?.let {
            startActivity(NearbyActivity.newIntent(it))
        }
    }

    private fun onWeixinClicked() {
        context?.let {
            startActivity(CreateDatingActivity.newIntent(it))
        }
    }

    private fun onQQClicked() {
        Log.d("TTTT", "AAAAAAAAAAAAAAAAAAAAAAAA")
        context?.let {
            startActivity(SignupActivity.newIntent(it))
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = SigninFragment()
    }
}
