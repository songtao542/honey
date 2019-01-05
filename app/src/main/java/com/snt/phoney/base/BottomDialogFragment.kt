package com.snt.phoney.base

import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.di.Injectable
import javax.inject.Inject

open class BottomDialogFragment : NoInjectBottomDialogFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

}