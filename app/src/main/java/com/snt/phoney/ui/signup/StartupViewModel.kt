package com.snt.phoney.ui.signup

import android.text.TextUtils
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.SigninUseCase
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.getAndroidVersion
import com.snt.phoney.extensions.getInstanceId
import com.snt.phoney.extensions.getVersionName
import com.snt.phoney.utils.life.SingleLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StartupViewModel @Inject constructor(private val usecase: SigninUseCase) : AppViewModel() {

    val user = SingleLiveData<User>()

    init {
        user.value = usecase.getUser()
    }

    fun signupByThirdPlatform(openId: String, thirdToken: String, plate: String, nickName: String, headPic: String) {
        val mobilePlate = "android"
        val osVersion = application.getAndroidVersion()
        val deviceToken = application.getInstanceId()
        val version = application.getVersionName()

        usecase.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic, deviceToken, osVersion, version, mobilePlate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    if (it.code == 200) {
                        usecase.setUser(it.data)
                        user.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }.disposedBy(disposeBag)
    }

    fun clearUser() {
        user.value = null
        usecase.setUser(null)
    }


    fun lock() {
        usecase.lock()
    }

}
