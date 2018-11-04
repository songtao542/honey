package com.snt.phoney.extensions

import android.support.design.widget.Snackbar
import android.app.Activity
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.*

fun View.snackbar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT, init: Snackbar.() -> Unit = {}): Snackbar {
    val snack = Snackbar.make(this, text, duration)
    snack.init()
    snack.show()
    return snack
}

fun View.snackbar(
    @StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT,
    init: Snackbar.() -> Unit = {}
): Snackbar {
    val snack = Snackbar.make(this, resId, duration)
    snack.init()
    snack.show()
    return snack
}

fun Fragment.snackbar(
    text: CharSequence,
    duration: Int = Snackbar.LENGTH_LONG,
    init: Snackbar.() -> Unit = {}
): Snackbar {
    return view!!.snackbar(text, duration, init)
}

fun Fragment.snackbar(
    @StringRes text: Int, duration: Int = Snackbar.LENGTH_LONG,
    init: Snackbar.() -> Unit = {}
): Snackbar {
    return view!!.snackbar(text, duration, init)
}

fun Activity.snackbar(
    text: CharSequence,
    duration: Int = Snackbar.LENGTH_LONG,
    init: Snackbar.() -> Unit = {}
): Snackbar {
    val decorView = window.decorView
    if (decorView is ViewGroup) {
        decorView.findViewById<ViewGroup>(android.R.id.content)?.let {
            return it.snackbar(text, duration, init)
        }
    }
    return decorView.snackbar(text, duration, init)
}

fun Activity.snackbar(
    view: View,
    text: CharSequence,
    duration: Int = Snackbar.LENGTH_LONG,
    init: Snackbar.() -> Unit = {}
): Snackbar {
    return view.snackbar(text, duration, init)
}

fun Activity.snackbar(
    view: View, @StringRes text: Int,
    duration: Int = Snackbar.LENGTH_LONG,
    init: Snackbar.() -> Unit = {}
): Snackbar {
    return view.snackbar(text, duration, init)
}

fun Activity.hasNavigationBar(): Boolean {
    //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
    val hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey()
    val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
    if (!hasMenuKey && !hasBackKey) {
        return true
    }
    return false
}
