package com.snt.phoney.ui.signup

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.SigninUseCase
import com.snt.phoney.extensions.getAndroidVersion
import com.snt.phoney.extensions.getInstanceId
import com.snt.phoney.extensions.getVersionName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StartupViewModel @Inject constructor(private val signinUseCase: SigninUseCase) : AppViewModel() {

    val user = MutableLiveData<User>()

    init {
        user.value = signinUseCase.getUser()
    }

    fun signupByThirdPlatform(openId: String, thirdToken: String, plate: String, nickName: String, headPic: String): Disposable? {
        val mobilePlate = "android"
        val osVersion = application.getAndroidVersion()
        val deviceToken = application.getInstanceId()
        val version = application.getVersionName()

        return signinUseCase.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic, deviceToken, osVersion, version, mobilePlate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == 200) {
                        signinUseCase.setUser(it.data)
                        user.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }


}
