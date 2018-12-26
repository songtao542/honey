package com.snt.phoney.base

import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

const val EXTRA_ARGUMENT = "argument"
const val EXTRA_PAGE = "page"

open class CommonActivity : CommonNoViewModelActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

}


