package com.snt.phoney.extensions

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Activity.toast(text: String) {
    toast(text, Toast.LENGTH_LONG)
}

fun Activity.toast(text: String, duration: Int) {
    Toast.makeText(this.applicationContext, text, if (duration == Toast.LENGTH_LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(text: String) {
    toast(text, Toast.LENGTH_LONG)
}

fun Fragment.toast(text: String, duration: Int) {
    Toast.makeText(requireContext().applicationContext, text, if (duration == Toast.LENGTH_LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}