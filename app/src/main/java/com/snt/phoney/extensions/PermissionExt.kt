package com.snt.phoney.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_SETTINGS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
)

fun Context.checkAppPermission(vararg permissions: String): Boolean {
    var requestList = ArrayList<String>()
    if (permissions.isNotEmpty()) {
        for (permission in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                requestList.add(permission)
            }
        }
    } else {
        for (permission in com.snt.phoney.extensions.permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                requestList.add(permission)
            }
        }
    }
    if (requestList.isEmpty()) {
        return true
    }
    return false
}

fun Activity.checkAndRequestPermission(vararg permissions: String) {
    var requestList = ArrayList<String>()
    if (permissions.isNotEmpty()) {
        for (permission in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                requestList.add(permission)
            }
        }
    } else {
        for (permission in com.snt.phoney.extensions.permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                requestList.add(permission)
            }
        }
    }
    if (!requestList.isEmpty()) {
        var array = Array(requestList.size) {
            requestList[it]
        }
        ActivityCompat.requestPermissions(this, array, 123)
    }
}

fun Fragment.checkAndRequestPermission(vararg permissions: String) {
    activity?.let { activity ->
        var requestList = ArrayList<String>()
        if (permissions.isNotEmpty()) {
            for (permission in permissions) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, permission)) {
                    requestList.add(permission)
                }
            }
        } else {
            for (permission in com.snt.phoney.extensions.permissions) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, permission)) {
                    requestList.add(permission)
                }
            }
        }
        if (!requestList.isEmpty()) {
            var array = Array(requestList.size) {
                requestList[it]
            }
            this.requestPermissions(array, 123)
        }
    }
}
