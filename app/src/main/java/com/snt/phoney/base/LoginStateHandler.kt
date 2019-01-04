package com.snt.phoney.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.snt.phoney.api.ACTION_LOGIN_STATE_INVALID
import com.snt.phoney.ui.signup.SignupActivity
import java.lang.ref.WeakReference

class LoginStateHandler(activity: Activity) : BroadcastReceiver() {
    private val activity: WeakReference<Activity> = WeakReference(activity)

    init {
        activity.registerReceiver(this, IntentFilter(ACTION_LOGIN_STATE_INVALID))
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        activity.get()?.let { activity ->
            activity.startActivity(SignupActivity.newIntent(activity))
            activity.finish()
        }
    }

    fun unregisterReceiver() {
        activity.get()?.unregisterReceiver(this)
    }

}