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


import android.arch.lifecycle.LiveData
import com.snt.phoney.api.Api
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.UserCredentialRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserCredentialRepositoryImpl @Inject constructor(private val api: Api) : UserCredentialRepository {

    override fun signin(username: String, password: String): Single<Response<User>> {
        return Single.create<Response<User>> {
            it.onSuccess(Response(User(id = "1234567890", username = username, password = password)))
        }
    }

//    override fun resetPassword(key: String, password: String): Single<Response<ResponseCode>> {
//        return Single.create<Response<ResponseCode>> {
//
//        }
//    }

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