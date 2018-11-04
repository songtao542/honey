package com.snt.phoney.ui.signin

import android.os.Bundle
import android.view.WindowManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import kotlinx.android.synthetic.main.activity_startup.*


class StartupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        signin.setOnClickListener {
            startActivity(SigninActivity.newIntent(this))
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}
