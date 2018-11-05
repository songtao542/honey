package com.snt.phoney.ui.vip

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.base.EXTRA_PAGE
import com.snt.phoney.base.FragmentFactory
import com.snt.phoney.base.Page
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.setLayoutFullscreen


class VipActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context, page: Page): Intent {
            return BaseActivity.newIntent(context, VipActivity::class.java, page)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()
        setContentView(R.layout.activity_vip)
        if (savedInstanceState == null) {
            val page = intent?.getIntExtra(EXTRA_PAGE, 1) ?: -1
            val fragment = FragmentFactory.create(page)
            addFragmentSafely(R.id.container, fragment, fragment::class.simpleName!!)
        }
    }

}
