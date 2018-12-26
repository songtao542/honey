package com.snt.phoney.base

import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setLayoutFullscreen
import com.snt.phoney.extensions.setStatusBarColor

abstract class CommonNoViewModelActivity : BaseNoViewModelActivity() {

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
