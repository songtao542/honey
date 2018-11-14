package com.snt.phoney.ui.signin

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

class StartupViewModel @Inject constructor(private val application: Application, private val signupUseCase: SignupUseCase) : ViewModel() {





    fun signupByThirdPlatform(openId: String, thirdToken: String, plate: String, nickName: String, headPic: String): Disposable? {
        val mobilePlate = "android"
        val osVersion = application.getAndroidVersion()
        val deviceToken = application.getInstanceId()
        val version = application.getVersionName()

        return signupUseCase.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic, deviceToken, osVersion, version, mobilePlate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "signupByThirdPlatform==>$it")

                }

    }


}
