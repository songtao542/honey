package com.snt.phoney.base

import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.di.Injectable
import javax.inject.Inject

abstract class BaseDialogFragment : NoInjectBaseDialogFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
}