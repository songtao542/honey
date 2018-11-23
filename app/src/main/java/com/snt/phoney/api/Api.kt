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
import com.snt.phoney.domain.model.*
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

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

    /**
     *@param sex -1，未设置；0，男性；1，女性
     */
    @FormUrlEncoded
    @POST("users/setSex")
    fun setUserSex(@Field("token") token: String,
                   @Field("sex") sex: Int): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/setFeatures")
    fun setUserFeatures(@Field("token") token: String,
                        @Field("height") height: String,
                        @Field("weight") weight: String,
                        @Field("age") age: String,
                        @Field("cup") cup: String): Single<Response<String>>


    /**
     *@param citys 城市列表id用逗号分隔
     *@param career 职业 对于文字，接口返回
     *@param program 宣言 对应为文字 接口返回
     */
    @FormUrlEncoded
    @POST("users/setBaseInfo")
    fun setUserInfo(@Field("token") token: String,
                    @Field("citys") cities: String,
                    @Field("career") career: String,
                    @Field("program") program: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/other/listCareer")
    fun listCareer(@Field("token") token: String): Single<Response<List<Career>>>

    @FormUrlEncoded
    @POST("users/other/listIntroduces")
    fun listPurpose(@Field("token") token: String): Single<Response<List<Purpose>>>


    @FormUrlEncoded
    @POST("sms/sendMsg")
    fun sendMsg(@Field("phone") phone: String): Single<Response<String>>


    /**
     *@param   token:String	用户token
     *@param   lat:String	注意经纬度是反的（当type=3 必填）
     *@param   lng:String	（当type=3 必填）
     *@param   type:String	type 0 默认全部（按时间倒叙） 1 今日新人 2 人气最高 3 距离最近 4 身材最好 5 更多筛选 6 查找城市
     *@param   page:String	从1开始
     *@param   city:String	城市（当type=6 必填）
     *@param   height_start:String	身高开始（当type=5 必填）
     *@param   height_end:String	身高结束（当type=5 必填）
     *@param   age_start:String	年龄开始（当type=5 必填）
     *@param   age_end:String	年龄结束（当type=5 必填）
     *@param   cup_start:String	罩杯开始（当type=5 必填）30A---45G
     *@param   cup_end:String	罩杯结束（当type=5 必填）
     */
    @FormUrlEncoded
    @POST("users/listUsers")
    fun listUser(
            @Field("token") token: String,
            @Field("lat") latitude: String,
            @Field("lng") longitude: String,
            @Field("type") type: String,
            @Field("page") page: String,
            @Field("city") city: String,
            @Field("height_start") heightStart: String,
            @Field("height_end") heightEnd: String,
            @Field("age_start") ageStart: String,
            @Field("age_end") ageEnd: String,
            @Field("cup_start") cupStart: String,
            @Field("cup_end") cupEnd: String
    ): Single<Response<List<User>>>


    @FormUrlEncoded
    @POST("users/third/register")
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
    @POST("sms/registerWithMsgCode")
    fun signupByMsgCode(@Field("phone") phone: String,
                        @Field("msg_id") msgId: String,
                        @Field("code") code: String,
                        @Field("deviceToken") deviceToken: String,
                        @Field("osVersion") osVersion: String,
                        @Field("version") version: String,
                        @Field("mobilePlate") mobilePlate: String): Single<Response<User>>


}