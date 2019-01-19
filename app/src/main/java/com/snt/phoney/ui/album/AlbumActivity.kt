package com.snt.phoney.ui.album

import android.os.Bundle
import android.view.View
import com.snt.phoney.R
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.utils.data.Constants
import cust.app.swipeback.SwipeBackHelper
import cust.app.swipeback.SwipeBackLayout


class AlbumActivity : CommonActivity(), SwipeBackLayout.OnSwipeBackListener {

    private var openSwipeBack = false

    private lateinit var swipeBackHelper: SwipeBackHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        //fixOrientation()
        val page = intent?.getIntExtra(Constants.Extra.PAGE, -1) ?: -1
        if (page == Page.ALBUM_VIEWER.ordinal) {
            openSwipeBack = true
        }

        super.onCreate(savedInstanceState)
    }

    override fun onConfigureTheme() {
        super.onConfigureTheme()
        if (openSwipeBack) {
            theme.applyStyle(R.style.SwipeBack, true)
        }
    }

    override fun setContentView(layoutResID: Int) {
        if (openSwipeBack) {
            swipeBackHelper = SwipeBackHelper(this)
            swipeBackHelper.setContentView(layoutResID)
            swipeBackHelper.setTrackingDirection(SwipeBackLayout.FROM_TOP)
            swipeBackHelper.setOnSwipeBackListener(this)
        } else {
            super.setContentView(layoutResID)
        }
    }

    override fun onViewPositionChanged(view: View?, swipeBackFraction: Float, swipeBackFactor: Float) {
        dispatchViewPositionChanged(view, swipeBackFraction, swipeBackFactor)
    }

    override fun onViewSwipeFinished(view: View?, isEnd: Boolean) {
        dispatchSwipeFinished(view, isEnd)
    }

    private fun dispatchViewPositionChanged(view: View?, swipeBackFraction: Float, swipeBackFactor: Float) {
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment.isVisible && fragment is SwipeBackLayout.OnSwipeBackListener) {
                (fragment as SwipeBackLayout.OnSwipeBackListener).onViewPositionChanged(view, swipeBackFraction, swipeBackFactor)
            }
        }
    }

    private fun dispatchSwipeFinished(view: View?, isEnd: Boolean) {
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment.isVisible && fragment is SwipeBackLayout.OnSwipeBackListener) {
                (fragment as SwipeBackLayout.OnSwipeBackListener).onViewSwipeFinished(view, isEnd)
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.alpha_out)
    }
}