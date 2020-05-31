package com.snt.phoney.base

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.snt.phoney.R

abstract class FullscreenDialogFragment : BaseDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullscreenDialog)
    }

    override fun onResume() {
        super.onResume()
        dialog?.let {
            val params = it.window?.attributes
            params?.width = WindowManager.LayoutParams.MATCH_PARENT
            params?.height = WindowManager.LayoutParams.MATCH_PARENT
            it.window?.attributes = params
        }
    }

}