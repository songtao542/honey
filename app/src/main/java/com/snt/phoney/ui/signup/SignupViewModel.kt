package com.snt.phoney.ui.signup

import android.text.TextUtils
import android.util.Log
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

class SignupViewModel @Inject constructor(private val usecase: SigninUseCase) : AppViewModel() {

    val verificationCode = MutableLiveData<String>()

    val user = MutableLiveData<User>()

    val error = MutableLiveData<String>()

    fun requestVerificationCode(phone: String): Disposable {
        return usecase.requestVerificationCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "requestVerificationCode success->$it")
                    if (it.code == 200) {
                        verificationCode.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }

    fun signup(phone: String, code: String): Disposable {
        val msgId = verificationCode.value ?: ""
        val mobilePlate = "android"
        val osVersion = application.getAndroidVersion()
        val deviceToken = application.getInstanceId()
        val appVersion = application.getVersionName()
        return usecase.signup(phone, msgId, code, deviceToken, osVersion, appVersion, mobilePlate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "signup success->$it")
                    if (it.code == 200) {
                        user.value = it.data
                        usecase.setUser(it.data)
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }

}
