/*
 *
 * Copyright (C) 2018. Faraday&Future
 * All rights reserved.
 * PROPRIETARY AND CONFIDENTIAL.
 * NOTICE: All information contained herein is, and remains the property of Faraday&Future Inc.
 * The intellectual and technical concepts contained herein are proprietary to Faraday&Future Inc.
 * and may be covered by U.S. and Foreign Patents, patents in process, and are protected
 * by trade secret and copyright law. Dissemination of this code or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Faraday&Future Inc.
 * Access to the source code contained herein is hereby forbidden to anyone except current
 * Faraday&Future Inc. employees or others who have executed Confidentiality and
 *  Non-disclosure agreements covering such access.
 *
 *
 */

package com.snt.phoney.repository


import androidx.lifecycle.LiveData
import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.UserCredentialRepository
import io.reactivex.Single
import retrofit2.http.Field
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCredentialRepositoryImpl @Inject constructor(private val api: Api) : UserCredentialRepository {

    override fun requestVerificationCode(phone: String): Single<Response<String>> {
        return api.sendMsg(phone)
    }

    override fun signup(phone: String, msgId: String, code: String, deviceToken: String, osVersion: String, version: String, mobilePlate: String): Single<Response<User>> {
        return api.registerWithMsgCode(phone, msgId, code, deviceToken, osVersion, version, mobilePlate)
    }

    override fun login(username: String, password: String): LiveData<Response<User>> {
        return api.login(username, password)
    }

    override fun resetPassword(username: String, password: String): LiveData<Response<String>> {
        return api.resetPassword(username, password)
    }


    override fun logout(username: String): LiveData<Response<String>> {
        return api.logout(username)
    }
}