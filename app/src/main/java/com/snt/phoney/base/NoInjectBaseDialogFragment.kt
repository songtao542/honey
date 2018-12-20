package com.snt.phoney.base

import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.snt.phoney.extensions.ClearableCompositeDisposable
import com.snt.phoney.extensions.autoCleared
import io.reactivex.disposables.CompositeDisposable

abstract class NoInjectBaseDialogFragment : DialogFragment() {

    private var clearableDisposeBag: ClearableCompositeDisposable by autoCleared(ClearableCompositeDisposable(CompositeDisposable()))

    protected val disposeBag: CompositeDisposable
        get() = clearableDisposeBag.compositeDisposable

    protected fun setMinWidth(width: Int) {
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }
}