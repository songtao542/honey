package com.snt.phoney.ui.wallet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setStatusBarColor


class WalletActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, WalletActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        setStatusBarColor(colorOf(android.R.color.white))
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, WalletFragment.newInstance())
                    .commitNow()
        }
    }

}
