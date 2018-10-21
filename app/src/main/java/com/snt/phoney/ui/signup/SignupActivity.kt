package com.snt.phoney.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setContentFragment
import com.snt.phoney.extensions.setStatusBarColor

class SignupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setStatusBarColor(colorOf(android.R.color.white))
        setContentFragment(R.id.containerLayout) { SignupFragment.newInstance() }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SignupActivity::class.java)
    }
}
