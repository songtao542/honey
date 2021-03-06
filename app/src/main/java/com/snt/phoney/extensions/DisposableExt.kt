package com.snt.phoney.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable?): Boolean {
    var success = false
    compositeDisposable?.apply {
        success = add(this)
    }
    return success
}