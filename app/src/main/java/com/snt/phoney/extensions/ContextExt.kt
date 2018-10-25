package com.snt.phoney.extensions

import android.content.Context
import android.os.Build
import android.util.TypedValue


fun Context.colorOf(id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(id)
    } else {
        this.resources.getColor(id)
    }
}

fun Context.dip(dip: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), resources.displayMetrics).toInt()
}