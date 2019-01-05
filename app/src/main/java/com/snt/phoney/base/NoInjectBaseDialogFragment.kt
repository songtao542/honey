package com.snt.phoney.base

import android.view.WindowManager
import androidx.fragment.app.DialogFragment

abstract class NoInjectBaseDialogFragment : DialogFragment() {

    @Suppress("unused")
    protected fun setMinWidth(width: Int) {
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }
}