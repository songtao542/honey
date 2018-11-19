package com.snt.phoney.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.forwardOnActivityResult
import com.snt.phoney.extensions.setStatusBarColor

class SignupActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SignupActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        setStatusBarColor(colorOf(android.R.color.white))
        addFragmentSafely(R.id.containerLayout, StartupFragment.newInstance(), "startup")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        //handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        forwardOnActivityResult(requestCode, resultCode, data)
    }
}
