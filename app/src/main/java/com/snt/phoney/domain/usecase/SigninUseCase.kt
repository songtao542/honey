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

import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

const val PLATFORM_QQ = "0"
const val PLATFORM_WECHAT = "1"
const val PLATFORM_WEIBO = "3"

class SigninUseCase @Inject constructor(private val repository: UserRepository) {
    fun signup(phone: String, msgId: String, code: String, deviceToken: String,
               osVersion: String, version: String, mobilePlate: String) =
            repository.signup(phone, msgId, code, deviceToken, osVersion, version, mobilePlate)


    fun signupByThirdPlatform(openId: String, //第三方openid（qq是uid）
                              thirdToken: String,
                              plate: String,
                              nickName: String,
                              headPic: String,
                              deviceToken: String,
                              osVersion: String,
                              version: String,
                              mobilePlate: String) = repository.signupByThirdPlatform(openId, thirdToken, plate, nickName, headPic, deviceToken, osVersion, version, mobilePlate)


    fun requestVerificationCode(phone: String): Single<Response<String>> = repository.requestVerificationCode(phone)

    var user: User?
        set(value) {
            repository.user = value
        }
        get() {
            return repository.user
        }
}