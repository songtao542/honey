package com.snt.phoney.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.usecase.PLATFORM_QQ
import com.snt.phoney.domain.usecase.PLATFORM_WECHAT
import com.snt.phoney.domain.usecase.PLATFORM_WEIBO
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.setup.SetupWizardActivity
import kotlinx.android.synthetic.main.activity_startup.*


class StartupFragment : BaseFragment() {

    private lateinit var viewModel: StartupViewModel
    private lateinit var qqViewModel: QQViewModel
    private lateinit var weiboViewModel: WeiboViewModel
    private lateinit var wxViewModel: WxViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_startup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartupViewModel::class.java)
        qqViewModel = ViewModelProviders.of(this, viewModelFactory).get(QQViewModel::class.java)
        weiboViewModel = ViewModelProviders.of(this, viewModelFactory).get(WeiboViewModel::class.java)
        wxViewModel = ViewModelProviders.of(this, viewModelFactory).get(WxViewModel::class.java)

        Log.d("TTTT", "xxxxxxxx11111111111xxxxxxxxxxxwxViewMode>$wxViewModel")

        signin.setOnClickListener {
            activity?.addFragmentSafely(R.id.containerLayout, SignupFragment.newInstance(), "signin")
        }

        qq.setOnClickListener {
            activity?.let { activity ->
                qqViewModel.login(activity)
            }
        }

        weibo.setOnClickListener {
            activity?.let { activity ->
                weiboViewModel.login(activity)
            }
        }

        wechat.setOnClickListener {
            activity?.let {
                wxViewModel.login()
            }
        }

        qqViewModel.user.observe(this, Observer {
            viewModel.signupByThirdPlatform(it.openId, it.thirdToken, PLATFORM_QQ, it.nickName, it.headPic)?.disposedBy(disposeBag)
        })

        weiboViewModel.user.observe(this, Observer {
            viewModel.signupByThirdPlatform(it.uid ?: "",
                    it.token ?: "",
                    PLATFORM_WEIBO,
                    it.name ?: "",
                    it.avatarLarge ?: "")?.disposedBy(disposeBag)
        })

        weiboViewModel.error.observe(this, Observer {
            snackbar(it)
        })

        wxViewModel.user.observe(this, Observer {
            viewModel.signupByThirdPlatform(it.openid ?: "",
                    it.accessToken ?: "",
                    PLATFORM_WECHAT,
                    it.nickname ?: "",
                    it.headimgurl ?: "")?.disposedBy(disposeBag)
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.user.observe(this, Observer { user ->
            snackbar("注册成功")
            //context?.let { context -> startActivity(MainActivity.newIntent(context)) }
            context?.let { context -> startActivity(SetupWizardActivity.newIntent(context, user)) }
            activity?.finish()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        qqViewModel.onActivityResult(requestCode, resultCode, data)
        weiboViewModel.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        @JvmStatic
        fun newInstance() = StartupFragment()
    }
}
