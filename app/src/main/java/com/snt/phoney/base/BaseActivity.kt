package com.snt.phoney.base

import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.extensions.hideSoftKeyboard
import javax.inject.Inject

abstract class BaseActivity : BaseNoViewModelActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideSoftKeyboard()
        return super.dispatchTouchEvent(ev)
    }
}


