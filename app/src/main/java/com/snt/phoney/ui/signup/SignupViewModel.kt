/*
 * Copyright (c) 2018. Faraday&Future
 * All rights reserved.
 * PROPRIETARY AND CONFIDENTIAL.
 * NOTICE: All information contained herein is, and remains the property of Faraday&Future Inc.
 * The intellectual and technical concepts contained herein are proprietary to Faraday&Future Inc.
 * and may be covered by U.S. and Foreign Patents, patents in process, and are protected
 * by trade secret and copyright law. Dissemination of this code or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Faraday&Future Inc.
 * Access to the source code contained herein is hereby forbidden to anyone except current
 * Faraday&Future Inc. employees or others who have executed Confidentiality and
 * Non-disclosure agreements covering such access.
 */

package com.snt.phoney.ui.signup

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class SignupViewModel @Inject constructor(private val application: Application, private val usecase: SigninUseCase) : ViewModel() {

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
                        usecase.user = it.data
                    } else if (!TextUtils.isEmpty(it.message)) {
                        error.value = it.message
                    }
                }
    }

}
