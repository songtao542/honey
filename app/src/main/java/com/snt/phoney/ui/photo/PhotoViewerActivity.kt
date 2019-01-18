package com.snt.phoney.ui.photo

import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.CommonNoViewModelActivity
import cust.app.swipeback.SwipeBackHelper
import cust.app.swipeback.SwipeBackLayout


class PhotoViewerActivity : CommonNoViewModelActivity() {

    private lateinit var swipeBackHelper: SwipeBackHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        //fixOrientation()
        super.onCreate(savedInstanceState)
    }

    override fun onConfigureTheme() {
        super.onConfigureTheme()
        theme.applyStyle(R.style.SwipeBack, true)
    }

    override fun setContentView(layoutResID: Int) {
        swipeBackHelper = SwipeBackHelper(this)
        swipeBackHelper.setContentView(layoutResID)
        swipeBackHelper.setTrackingDirection(SwipeBackLayout.FROM_TOP)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.alpha_out)
    }

}
