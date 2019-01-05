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
import com.snt.phoney.extensions.toast
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.ui.setup.SetupWizardActivity
import kotlinx.android.synthetic.main.fragment_startup.*


class StartupFragment : BaseFragment() {

    private lateinit var viewModel: StartupViewModel
    private lateinit var qqViewModel: QQViewModel
    private lateinit var weiboViewModel: WeiboViewModel
    private lateinit var wxViewModel: WechatViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_startup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartupViewModel::class.java)
        qqViewModel = ViewModelProviders.of(this, viewModelFactory).get(QQViewModel::class.java)
        weiboViewModel = ViewModelProviders.of(this, viewModelFactory).get(WeiboViewModel::class.java)
        wxViewModel = ViewModelProviders.of(this, viewModelFactory).get(WechatViewModel::class.java)

        signin.setOnClickListener {
            activity?.addFragmentSafely(R.id.containerLayout, SignupFragment.newInstance(), "signin")
        }

        qq.setOnClickListener {
            activity?.let { activity ->
                //清除其他授权时保存的用户，以免相互影响
                weiboViewModel.clearWeiboUser()
                wxViewModel.clearWxUser()
                qqViewModel.login(activity)
            }
        }

        weibo.setOnClickListener {
            activity?.let { activity ->
                //清除其他授权时保存的用户，以免相互影响
                wxViewModel.clearWxUser()
                weiboViewModel.login(activity)
            }
        }

        wechat.setOnClickListener {
            activity?.let {
                //清除其他授权时保存的用户，以免相互影响
                weiboViewModel.clearWeiboUser()
                wxViewModel.login()
            }
        }

        viewModel.user.value?.let { user ->
            if (user.isSexValid) {
                root.postDelayed({
                    if (user.validated) {
                        viewModel.lock()
                        context?.let { context -> startActivity(MainActivity.newIntent(context)) }
                    } else {
                        context?.let { context -> startActivity(SetupWizardActivity.newIntent(context, user)) }
                    }
                    activity?.finish()
                }, 1500)
                return
            } else {
                //清除无效的用户
                viewModel.clearUser()
            }
        }

        content.visibility = View.VISIBLE

        qqViewModel.user.observe(this, Observer { user ->
            user?.let {
                viewModel.signupByThirdPlatform(it.openId, it.thirdToken, PLATFORM_QQ, it.nickName, it.headPic)
            }
        })

        weiboViewModel.user.observe(this, Observer { user ->
            user?.let {
                viewModel.signupByThirdPlatform(it.uid ?: "",
                        it.token ?: "",
                        PLATFORM_WEIBO,
                        it.name ?: "",
                        it.avatarLarge ?: "")
            }
        })

        weiboViewModel.error.observe(this, Observer {
            snackbar(it)
        })

        wxViewModel.user.observe(this, Observer { user ->
            user?.let {
                viewModel.signupByThirdPlatform(it.openid ?: "",
                        it.accessToken ?: "",
                        PLATFORM_WECHAT,
                        it.nickname ?: "",
                        it.headimgurl ?: "")
            }
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.user.observe(this, Observer { it ->
            it?.let { user ->
                toast("登录成功")
                clearThirdPlatformUser()
                if (user.validated) {
                    context?.let { context -> startActivity(MainActivity.newIntent(context)) }
                } else {
                    context?.let { context -> startActivity(SetupWizardActivity.newIntent(context, user)) }
                }
                activity?.finish()
                return@let
            }
        })
    }

    private fun clearThirdPlatformUser() {
        wxViewModel.clearWxUser()
        weiboViewModel.clearWeiboUser()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        qqViewModel.onActivityResult(requestCode, resultCode, data)
        weiboViewModel.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        wxViewModel.resume()
    }

    companion object {
        @JvmStatic
        fun newInstance() = StartupFragment()
    }
}
