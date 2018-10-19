package com.snt.phoney.extensions

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

fun Activity.setStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        var windowParams = window.attributes
        // 设置透明状态栏
        windowParams?.let {
            var mask = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            var flag = it.flags and mask
            if (flag == mask) {
                it.flags = it.flags or mask
                window?.attributes = it
            }
        }
//        window.decorView.fitsSystemWindows = true

        // 设置状态栏颜色
        var contentView = this.findViewById<ViewGroup>(android.R.id.content)
        var resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        var statusBarHeight = this.resources.getDimensionPixelSize(resourceId)
        var statusBarView = contentView.findViewById<View>(com.snt.phoney.R.id.status_bar_holder)
        if (statusBarView == null) {
            statusBarView = View(this);
            var lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
            contentView.addView(statusBarView, lp)
        }
        statusBarView.setBackgroundColor(color)

        // 设置Activity layout的fitsSystemWindows
        var contentChild = contentView.getChildAt(0)
        contentChild.fitsSystemWindows = true
    }

}


fun Activity.colorOf(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(id)
    } else {
        this.resources.getColor(id)
    }
}