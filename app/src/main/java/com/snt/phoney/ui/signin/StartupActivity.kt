package com.snt.phoney.ui.signin

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.ui.signup.SignupViewModel
import com.snt.phoney.utils.data.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.activity_startup.*
import okhttp3.Response


class StartupActivity : BaseActivity() {

    lateinit var viewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignupViewModel::class.java)

        signin.setOnClickListener {
            startActivity(SigninActivity.newIntent(this))
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        qq.setOnClickListener {

        }
    }


}
