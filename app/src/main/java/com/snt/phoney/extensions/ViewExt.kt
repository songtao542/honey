package com.snt.phoney.extensions

import android.view.View

fun View.dip(dip: Float): Int {
    return android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, dip, resources.displayMetrics).toInt()
}

fun View.dip(dip: Int): Int {
    return android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), resources.displayMetrics).toInt()
}