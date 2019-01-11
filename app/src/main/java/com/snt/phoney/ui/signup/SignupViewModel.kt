package com.snt.phoney.ui.signup

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.SigninUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.getAndroidVersion
import com.snt.phoney.extensions.getInstanceId
import com.snt.phoney.extensions.getVersionName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignupViewModel @Inject constructor(private val usecase: SigninUseCase) : AppViewModel() {

    val verificationCode = MutableLiveData<String>()

    val user = MutableLiveData<User>()

    fun requestVerificationCode(phone: String) {
        usecase.requestVerificationCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                verificationCode.value = it.data
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }

    fun signup(phone: String, code: String) {
        val msgId = verificationCode.value ?: ""
        val mobilePlate = "android"
        val osVersion = application.getAndroidVersion()
        val deviceToken = application.getInstanceId()
        val appVersion = application.getVersionName()
        usecase.signup(phone, msgId, code, deviceToken, osVersion, appVersion, mobilePlate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            if (it.code == 200) {
                                user.value = it.data
                                usecase.setUser(it.data)
                            } else if (!TextUtils.isEmpty(it.message)) {
                                error.value = it.message
                            }
                        },
                        onError = {}
                ).disposedBy(disposeBag)
    }

}
