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

package com.snt.phoney.domain.repository


import com.snt.phoney.domain.model.AmountInfo
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import io.reactivex.Single

interface UserRepository {
    fun signup(phone: String,
               msgId: String,
               code: String,
               deviceToken: String,
               osVersion: String,
               version: String,
               mobilePlate: String): Single<Response<User>>

    fun bindPhone(msgId: String,
                  code: String,
                  phone: String,
                  uuid: String,
                  token: String): Single<Response<String>>

    fun signupByThirdPlatform(openId: String, //第三方openid（qq是uid）
                              thirdToken: String,
                              plate: String,
                              nickName: String,
                              headPic: String,
                              deviceToken: String,
                              osVersion: String,
                              version: String,
                              mobilePlate: String): Single<Response<User>>

    fun setUserSex(token: String, sex: Int): Single<Response<String>>

    fun setUserFeatures(token: String,
                        height: Int,
                        weight: Int,
                        age: Int,
                        cup: String): Single<Response<String>>

    fun setUserInfo(token: String,
                    cities: String,
                    career: String,
                    program: String): Single<Response<String>>


    fun setFullUserInfo(token: String,
                        height: Int,
                        weight: Double,
                        age: Int,
                        cup: String,
                        cities: String,
                        introduce: String,
                        career: String,
                        program: String,
                        wechatAccount: String,
                        nickname: String): Single<Response<String>>

    fun listUser(
            token: String,
            latitude: String,
            longitude: String,
            type: String,
            page: String,
            city: String,
            heightStart: String,
            heightEnd: String,
            ageStart: String,
            ageEnd: String,
            cupStart: String,
            cupEnd: String
    ): Single<Response<List<User>>>


    fun getUserInfo(token: String,
                    uid: String,    //	用户uuid
                    latitude: Double,
                    longitude: Double): Single<Response<User>>

    fun setPhotoPermission(token: String,
                           photoPermission: Int,
                           money: Double,
                           photoId: String): Single<Response<String>>

    fun getUserAmountInfo(token: String): Single<Response<AmountInfo>>


    fun listFollow(token: String): Single<Response<List<User>>>
    fun listVisitor(token: String): Single<Response<List<User>>>

    fun follow(token: String, uuid: String): Single<Response<Boolean>>

//    fun login(username: String, password: String): LiveData<Response<User>>
//    fun resetPassword(key: String, password: String): LiveData<Response<String>>
//    fun logout(username: String): LiveData<Response<String>>

    fun requestVerificationCode(phone: String): Single<Response<String>>

    fun deleteUser(token: String): Single<Response<String>>

    var user: User?

}