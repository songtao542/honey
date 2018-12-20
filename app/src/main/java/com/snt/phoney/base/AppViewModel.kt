package com.snt.phoney.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

open class AppViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext = Job() + Dispatchers.Main

    @Inject
    lateinit var application: Application

    @Inject
    lateinit var context: Context

    protected val disposeBag: CompositeDisposable = CompositeDisposable()

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


    override fun onCleared() {
        disposeBag.dispose()
        disposeBag.clear()
    }
}