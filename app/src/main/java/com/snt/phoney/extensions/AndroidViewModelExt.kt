package com.snt.phoney.extensions

import android.content.Context
import androidx.lifecycle.AndroidViewModel

fun AndroidViewModel.getContext(): Context {
    return getApplication()
}