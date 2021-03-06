package com.snt.phoney.extensions

import android.content.Context
import android.os.Build
import android.util.Log
import com.snt.phoney.utils.data.InstanceID


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

fun Context.getAndroidVersion(): String {
    return Build.VERSION.RELEASE
}

fun Context.getInstanceId(): String {
    return InstanceID.getId(this)
}


