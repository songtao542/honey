package com.snt.phoney.ui.startup

import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.disposedBy
import io.reactivex.Observable

class StartupActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        Observable.just(1).subscribe().disposedBy(disposeBag)
    }
}
