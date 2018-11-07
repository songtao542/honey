package com.snt.phoney.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.extensions.setStatusBarColor

const val EXTRA_ARGUMENT = "argument"
const val EXTRA_PAGE = "page"

class CommonActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context, page: Page, argument: Bundle? = null): Intent {
            val intent = BaseActivity.newIntent(context, CommonActivity::class.java, page)
            argument?.let {
                intent.putExtra(EXTRA_ARGUMENT, argument)
            }
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutFullscreen()
        setContentView(R.layout.activity_common)
        setStatusBarColor(colorOf(android.R.color.white))
        if (savedInstanceState == null) {
            val page = intent?.getIntExtra(EXTRA_PAGE, 1) ?: -1
            val argument = intent?.getBundleExtra(EXTRA_ARGUMENT)
            val fragment = FragmentFactory.create(page, argument)
            addFragmentSafely(R.id.container, fragment, fragment::class.simpleName!!)
        }
    }
}