package com.snt.phoney.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment

fun Fragment.isInternetAvailable(): Boolean? {
    return activity?.isInternetAvailable()
}

fun Application.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Activity.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}