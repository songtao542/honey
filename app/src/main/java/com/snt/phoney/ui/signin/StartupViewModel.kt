package com.snt.phoney.ui.signin

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.SetupWizardUseCase
import com.snt.phoney.domain.usecase.SigninUseCase
import com.snt.phoney.extensions.getAndroidVersion
import com.snt.phoney.extensions.getInstanceId
import com.snt.phoney.extensions.getVersionName
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import com.snt.phoney.di.SignupScope

//@SignupScope
class StartupViewModel @Inject constructor(private val application: Application, private val signinUseCase: SigninUseCase) : ViewModel() {

    val error = MutableLiveData<String>()

    val user = MutableLiveData<User>()

    fun signupByThirdPlatform(openId: String, thirdToken: String, plate: String, nickName: String, headPic: String): Disposable? {
        val mobilePlate = "android"
        val osVersion = application.getAndroidVersion()
        val deviceToken = application.getInstanceId()
        val version = application.getVersionName()

        return signinUseCase.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic, deviceToken, osVersion, version, mobilePlate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    Log.d("TTTT", "signupByThirdPlatform==>$it")
                    if (it.code == 200) {
                        signinUseCase.user = it.data
                        user.value = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }


}
