package com.snt.phoney.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity


class UserInfoActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, UserInfoActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, UserInfoFragment.newInstance())
                    .commitNow()
        }
    }

}
