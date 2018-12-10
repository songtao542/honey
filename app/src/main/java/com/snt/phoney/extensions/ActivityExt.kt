package com.snt.phoney.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity


//fun Activity.enterFullscreen() {
//    window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//    window?.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

//    window.decorView.systemUiVisibility = (
//            View.SYSTEM_UI_FLAG_IMMERSIVE
// Set the content to appear under the system bars so that the
// content doesn't resize when the system bars hide and show.
//             View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
// Hide the nav bar and status bar
//            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//             View.SYSTEM_UI_FLAG_FULLSCREEN
//            )
//}
//
//fun Activity.exitFullscreen() {
//    window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//    window?.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

//    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//}

fun Activity.hideSystemUI() {
    val option = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN
    window.decorView.systemUiVisibility = option
}

fun Activity.showSystemUI() {
    val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    window.decorView.systemUiVisibility = option
}

fun Activity.hideActionBar() {
    if (this is AppCompatActivity) {
        supportActionBar?.hide()
    } else {
        actionBar.hide()
    }
}

fun Activity.showActionBar() {
    if (this is AppCompatActivity) {
        supportActionBar?.show()
    } else {
        actionBar.show()
    }
}

fun Activity.isActionBarShowing(): Boolean {
    return if (this is AppCompatActivity) {
        supportActionBar?.isShowing ?: false
    } else {
        actionBar.isShowing
    }
}

class AndroidBug5497Workaround private constructor(activity: Activity) {

    private val mChildOfContent: View
    private var usableHeightPrevious: Int = 0
    private val frameLayoutParams: FrameLayout.LayoutParams

    init {
        val content = activity.findViewById<View>(android.R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener {
            possiblyResizeChildOfContent()
        }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return r.bottom - r.top
    }

    companion object {
        /**
         * For more information, see https://code.google.com/p/android/issues/detail?id=5497
         * To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
         */
        fun assistActivity(activity: Activity) {
            AndroidBug5497Workaround(activity)
        }
    }

}
