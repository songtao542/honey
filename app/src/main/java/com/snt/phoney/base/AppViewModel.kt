package com.snt.phoney.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import javax.inject.Inject

open class AppViewModel : ViewModel() {

    @Inject
    lateinit var application: Application

    @Inject
    lateinit var context: Context


    /**
     * 成员(application 和 context)注入完成后调用
     */
    @Inject
    fun memberInjectionComplete() {
        initialize()
    }

    /**
     * 如果想在(application 和 context)注入完成后做一些操作，可以 override 该方法
     */
    open fun initialize() {
    }

}