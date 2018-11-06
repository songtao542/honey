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

package com.snt.phoney.ui.signin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.usecase.LoginUseCase
import io.reactivex.Single
import javax.inject.Inject

class SigninViewModel @Inject constructor(private val usecase: LoginUseCase, private val application: Application) : ViewModel() {

    fun signin(email: String, password: String): Single<Response<User>> {
        return usecase.signin(email, password)
    }

    fun login(email: String, password: String): LiveData<Response<User>> {
        return usecase.login(email, password)
    }

    fun updateUser(user: User?) = usecase.updateUser(user)

    val user: User?
        get() = usecase.user

}
