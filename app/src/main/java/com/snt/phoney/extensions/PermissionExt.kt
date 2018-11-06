package com.snt.phoney.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_SETTINGS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE
)

fun Context.checkAppPermission(): Boolean {
    var requestList = ArrayList<String>()
    for (permission in permissions) {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
            requestList.add(permission)
        }
    }
    if (!requestList.isEmpty()) {
        return true
    }
    return false
}

fun Activity.checkAndRequestPermission() {
    var requestList = ArrayList<String>()
    for (permission in permissions) {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
            requestList.add(permission)
        }
    }
    if (!requestList.isEmpty()) {
        var array = Array(requestList.size) {
            requestList[it]
        }
        ActivityCompat.requestPermissions(this, array, 123)
    }
}
