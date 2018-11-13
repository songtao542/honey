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

package com.snt.phoney.domain.usecase

import androidx.lifecycle.LiveData
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.domain.repository.UserCredentialRepository
import io.reactivex.Single
import javax.inject.Inject

class SigninUseCase @Inject constructor(private val repository: UserCredentialRepository, private val cache: CacheRepository) {
    fun signup(phone: String, msgId: String, code: String, deviceToken: String,
               osVersion: String, version: String, mobilePlate: String): Single<Response<User>> =
            repository.signup(phone, msgId, code, deviceToken, osVersion, version, mobilePlate)

    fun login(username: String, password: String): LiveData<Response<User>> = repository.login(username, password)

    fun requestVerificationCode(phone: String): Single<Response<String>> = repository.requestVerificationCode(phone)

    fun updateUser(user: User?) {
        cache.user = user
    }

    val user: User?
        get() = cache.user
}