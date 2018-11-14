/*
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
 * Non-disclosure agreements covering such access.
 */

package com.snt.phoney.api

import androidx.lifecycle.LiveData
import com.snt.phoney.domain.model.Province
import com.snt.phoney.domain.model.Response
import com.snt.phoney.domain.model.User
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
//    @POST("user/login")
//    fun login(@Field("username") username: String, @Field("password") password: String): Single<Response<User>>

    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String, @Field("password") password: String): LiveData<Response<User>>

    @FormUrlEncoded
    @POST("user/login")
    fun resetPassword(@Field("username") username: String, @Field("password") password: String): LiveData<Response<String>>


    @FormUrlEncoded
    @POST("user/login")
    fun logout(@Field("username") username: String): LiveData<Response<String>>


    @GET("city/listCities")
    fun listCities(): Single<Response<List<Province>>>


    @FormUrlEncoded
    @POST("sms/bindMobile")
    fun bindPhone(@Field("msg_id") msgId: String,
                  @Field("code") code: String,
                  @Field("phone") phone: String,
                  @Field("uuid") uuid: String,
                  @Field("token") token: String): Single<Response<String>>


    @FormUrlEncoded
    @POST("sms/sendMsg")
    fun sendMsg(@Field("phone") phone: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("sms/signupByMsgCode")
    fun signupByThirdPlatform(@Field("openId") openId: String, //第三方openid（qq是uid）
                              @Field("third_token") thirdToken: String,
                              @Field("plate") plate: String,
                              @Field("nickName") nickName: String,
                              @Field("headPic") headPic: String,
                              @Field("deviceToken") deviceToken: String,
                              @Field("osVersion") osVersion: String,
                              @Field("version") version: String,
                              @Field("mobilePlate") mobilePlate: String): Single<Response<User>>

    @FormUrlEncoded
    @POST("sms/signupByMsgCode")
    fun signupByMsgCode(@Field("phone") phone: String,
                        @Field("msg_id") msgId: String,
                        @Field("code") code: String,
                        @Field("deviceToken") deviceToken: String,
                        @Field("osVersion") osVersion: String,
                        @Field("version") version: String,
                        @Field("mobilePlate") mobilePlate: String): Single<Response<User>>


}