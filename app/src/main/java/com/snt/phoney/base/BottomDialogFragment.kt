package com.snt.phoney.base

import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snt.phoney.di.Injectable
import com.snt.phoney.extensions.ClearableCompositeDisposable
import com.snt.phoney.extensions.autoCleared
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BottomDialogFragment : BottomSheetDialogFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var clearableDisposeBag: ClearableCompositeDisposable by autoCleared(ClearableCompositeDisposable(CompositeDisposable()))

    protected val disposeBag: CompositeDisposable
        get() = clearableDisposeBag.compositeDisposable

    protected fun setMinWidth(width: Int) {
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return BottomSheetDialog(requireContext(), theme)
//    }

}