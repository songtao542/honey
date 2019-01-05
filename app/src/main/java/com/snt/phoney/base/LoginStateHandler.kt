package com.snt.phoney.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.snt.phoney.api.ACTION_LOGIN_STATE_INVALID
import com.snt.phoney.extensions.registerReceiver
import com.snt.phoney.extensions.unregisterLocalBroadcastReceiver
import com.snt.phoney.ui.signup.SignupActivity
import java.lang.ref.WeakReference

class LoginStateHandler(activity: BaseNoViewModelActivity) : BroadcastReceiver() {
    private val activity: WeakReference<BaseNoViewModelActivity> = WeakReference(activity)

    init {
        activity.registerReceiver(this, ACTION_LOGIN_STATE_INVALID)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        activity.get()?.let { activity ->
            activity.userAccessor.setUser(null)
            activity.startActivity(SignupActivity.newIntent(activity))
            activity.finish()
        }
    }

    fun unregisterReceiver() {
        activity.get()?.let {
            it.unregisterLocalBroadcastReceiver(this)
        }
    }
}