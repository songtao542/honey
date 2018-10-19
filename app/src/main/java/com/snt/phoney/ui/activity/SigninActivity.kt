package com.snt.phoney.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.setContentFragment
import com.snt.phoney.ui.fragment.LoginFragment

class SigninActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SigninActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        setContentFragment(R.id.containerLayout) { LoginFragment.newInstance() }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData = intent.data
        if (Intent.ACTION_VIEW == appLinkAction && appLinkData != null && appLinkData.host != null) {

        }
    }
}
