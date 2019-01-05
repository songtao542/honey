package com.snt.phoney.base

import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class NoInjectBottomDialogFragment : BottomSheetDialogFragment() {

    @Suppress("unused")
    protected fun setMinWidth(width: Int) {
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

}