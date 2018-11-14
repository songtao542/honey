package com.snt.phoney.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.View.*
import android.view.WindowManager


fun Activity.enterFullscreen() {
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
}

fun Activity.exitFullscreen() {
//    window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//    window?.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)

//    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}
