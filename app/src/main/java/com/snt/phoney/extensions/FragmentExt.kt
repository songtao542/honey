package com.snt.phoney.extensions

import android.os.Build
import android.support.v4.app.Fragment
import android.util.TypedValue

fun Fragment.colorOf(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context?.getColor(id) ?: resources.getColor(id)
    } else {
        resources.getColor(id)
    }
}

fun Fragment.dip(dip: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), resources.displayMetrics).toInt()
}