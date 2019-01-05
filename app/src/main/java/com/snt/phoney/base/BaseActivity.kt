package com.snt.phoney.base

import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class BaseActivity : BaseNoViewModelActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

}


