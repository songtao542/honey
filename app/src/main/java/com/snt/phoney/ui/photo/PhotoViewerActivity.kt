package com.snt.phoney.ui.photo

import android.view.View
import com.snt.phoney.R
import com.snt.phoney.base.CommonNoViewModelActivity
import cust.app.swipeback.SwipeBackHelper
import cust.app.swipeback.SwipeBackLayout


class PhotoViewerActivity : CommonNoViewModelActivity(), SwipeBackLayout.OnSwipeBackListener {

    private lateinit var swipeBackHelper: SwipeBackHelper

    override fun onConfigureTheme() {
        super.onConfigureTheme()
        theme.applyStyle(R.style.SwipeBack, true)
    }

    override fun setContentView(layoutResID: Int) {
        swipeBackHelper = SwipeBackHelper(this)
        swipeBackHelper.setContentView(layoutResID)
        swipeBackHelper.setTrackingDirection(SwipeBackLayout.FROM_TOP)
        swipeBackHelper.setOnSwipeBackListener(this)
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
