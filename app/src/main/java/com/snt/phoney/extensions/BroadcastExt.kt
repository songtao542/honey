package com.snt.phoney.extensions

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.parcel.Parcelize

const val ACTION = "com.snt.phoney.ACTION."
const val EXTRA_EVENT = "extra_event"


inline fun buildAction(action: String): String {
    return "$ACTION$action"
}

inline fun Context.sendBroadcast(action: String, event: Event? = null) {
    val intent = Intent(buildAction(action)).apply {
        val ev = event?.apply { this.action = action } ?: Event(action = action)
        putExtra(EXTRA_EVENT, ev)
    }
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
}

inline fun Fragment.sendBroadcast(action: String, event: Event? = null) {
    context?.sendBroadcast(action, event)
}

@Parcelize
data class Event(
        var id: Int = 0,
        var action: String,
        var message: String? = null
) : Parcelable

class ClearableBroadcast : AutoClearedValue.Clearable {

    private var receivers = HashMap<Context, BroadcastReceiver>()

    override fun clear() {
        for ((context, receiver) in receivers) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver)
        }
        receivers.clear()
    }

    fun registerReceiver(context: Context, action: String, receiver: ((event: Event?) -> Unit)) {
        val realReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                receiver.invoke(intent?.getParcelableExtra(EXTRA_EVENT))
            }
        }
        receivers[context] = realReceiver
        LocalBroadcastManager.getInstance(context).registerReceiver(realReceiver, IntentFilter("$ACTION$action"))
    }

    fun registerReceiver(context: Context, actions: List<String>, receiver: ((event: Event?) -> Unit)) {
        val realReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                receiver.invoke(intent?.getParcelableExtra(EXTRA_EVENT))
            }
        }
        receivers[context] = realReceiver
        val intentFilter = IntentFilter()
        for (action in actions) {
            intentFilter.addAction(buildAction(action))
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(realReceiver, IntentFilter())
    }


}