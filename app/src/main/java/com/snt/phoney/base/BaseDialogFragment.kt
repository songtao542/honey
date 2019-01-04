package com.snt.phoney.base

import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.di.Injectable
import javax.inject.Inject

abstract class BaseDialogFragment : DialogFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected fun setMinWidth(width: Int) {
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }
}