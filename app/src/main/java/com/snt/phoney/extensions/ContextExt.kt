package com.snt.phoney.extensions

import android.content.Context
import android.os.Build
import android.util.Log
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


/**
 * 返回当前程序版本号
 */
fun Context.getVersionCode(): Int {
    var versioncode = 0
    try {
        val pm = this.packageManager
        val pi = pm.getPackageInfo(this.packageName, 0)
        //versioncode = pi.longVersionCode
        versioncode = pi.versionCode
    } catch (e: Exception) {
        Log.e(TAG, "getVersionCode,error:${e.message}")
    }
    return versioncode
}

/**
 * 返回当前程序版本名
 */
fun Context.getVersionName(): String {
    var versionName: String? = null
    try {
        val pm = this.packageManager
        val pi = pm.getPackageInfo(this.packageName, 0)
        versionName = pi.versionName
    } catch (e: Exception) {
        Log.e(TAG, "getVersionName,error:${e.message}")
    }
    return versionName ?: ""
}
