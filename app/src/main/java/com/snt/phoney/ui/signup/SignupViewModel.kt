package com.snt.phoney.ui.signup

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.usecase.SignupUseCase
import com.snt.phoney.extensions.getAndroidVersion
import com.snt.phoney.extensions.getInstanceId
import com.snt.phoney.extensions.getVersionName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignupViewModel @Inject constructor(private val application: Application, private val signupUseCase: SignupUseCase) : ViewModel() {

    fun getCities(): Disposable {
        return signupUseCase.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "getCities==>$it")
                }
    }

    fun signupByThirdPlatform(): Disposable? {
        val mobilePlate = "android"
        val osVersion = application.getAndroidVersion()
        val deviceToken = application.getInstanceId()
        val appVersion = application.getVersionName()

//        return signupUseCase.signupByThirdPlatform( )
        return null
    }


}
