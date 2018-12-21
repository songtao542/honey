package com.snt.phoney.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment


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


fun Context.getStatusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return when {
        resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
        else -> dip(24)
    }
}

fun Fragment.getStatusBarHeight(): Int {
    return requireContext().getStatusBarHeight()
}

fun Activity.setLayoutFullscreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        val background = resources.getDrawable(R.drawable.gradient_bg, theme)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = resources.getColor(android.R.color.transparent)
//        window.setBackgroundDrawable(background)

        val decorView = window.decorView
        val option = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun Activity.setLightStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val decorView = window.decorView
        val option = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT
    }
}