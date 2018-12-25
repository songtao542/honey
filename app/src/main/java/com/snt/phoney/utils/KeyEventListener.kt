package com.snt.phoney.utils

import android.view.KeyEvent

interface KeyEventListener {
    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean
    fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean
}