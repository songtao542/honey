package com.snt.phoney.extensions

import io.reactivex.disposables.CompositeDisposable

class ClearableCompositeDisposable(val compositeDisposable: CompositeDisposable) : AutoClearedValue.Clearable {

    override fun clear() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

}