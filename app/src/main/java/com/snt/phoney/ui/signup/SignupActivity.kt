package com.snt.phoney.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.*

class SignupActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SignupActivity::class.java)
    }

    override fun onApplyTheme(themeId: Int): Int = 0

    override fun shouldObserveLoginState(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()
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
