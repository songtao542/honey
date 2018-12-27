package com.snt.phoney.base

import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

open class CommonActivity : CommonNoViewModelActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

}


