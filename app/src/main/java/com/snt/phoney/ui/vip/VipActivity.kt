package com.snt.phoney.ui.vip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.ui.signup.SignupActivity


class VipActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, VipActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vip)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, VipFragment.newInstance())
                    .commitNow()
        }
    }

}
