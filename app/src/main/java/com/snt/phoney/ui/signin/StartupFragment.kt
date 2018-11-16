package com.snt.phoney.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.setup.SetupWizardActivity
import kotlinx.android.synthetic.main.activity_startup.*


class StartupFragment : BaseFragment() {

    lateinit var viewModel: StartupViewModel
    private lateinit var qqViewModel: QQViewModel
    private lateinit var weiboViewModel: WeiboViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_startup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartupViewModel::class.java)
        qqViewModel = ViewModelProviders.of(this).get(QQViewModel::class.java)
        weiboViewModel = ViewModelProviders.of(this).get(WeiboViewModel::class.java)

        signin.setOnClickListener {
            activity?.addFragmentSafely(R.id.containerLayout, SigninFragment.newInstance(), "signin")
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

        qqViewModel.user.observe(this, Observer {
            viewModel.signupByThirdPlatform(it.openId, it.thirdToken, it.plate, it.nickName, it.headPic)
        })

        weiboViewModel.weiboUser.observe(this, Observer {
            viewModel.signupByThirdPlatform(it.uid ?: "", it.token ?: "", "3", it.name
                    ?: "", it.avatarLarge ?: "")
        })

        weiboViewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.user.observe(this, Observer { user ->
            snackbar("注册成功")
            //context?.let { context -> startActivity(MainActivity.newIntent(context)) }
            context?.let { context -> startActivity(SetupWizardActivity.newIntent(context, user)) }
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
