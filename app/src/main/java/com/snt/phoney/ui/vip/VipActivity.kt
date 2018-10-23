package com.snt.phoney.ui.vip

import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity


class VipActivity : BaseActivity() {

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
