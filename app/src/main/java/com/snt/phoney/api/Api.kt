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
import retrofit2.http.*

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
    @POST("users/setUser")
    fun setFullUserInfo(@Field("token") token: String,
                        @Field("height") height: String,// 	身高
                        @Field("weight") weight: String,//	体重
                        @Field("age") age: String,//	年龄
                        @Field("cup") cup: String,//	罩杯
                        @Field("citys") cities: String,//	常在城市
                        @Field("introduce") introduce: String,//	个人介绍
                        @Field("career") career: String,//	职业
                        @Field("program") program: String,//	约会节目
                        @Field("account_wx") wechatAccount: String,//	微信
                        @Field("nickName") nickname: String  //	昵称
    ): Single<Response<String>>


    @FormUrlEncoded
    @POST("users/delUser")
    fun deleteUser(@Field("token") token: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/getUsersPhoto")
    fun getUserPhotoes(@Field("token") token: String): Single<Response<List<Photo>>>

    @FormUrlEncoded
    @POST("users/closePrivatePassword")
    fun closePrivatePassword(@Field("token") token: String): Single<Response<String>>

    /**
     * 获取访客列表
     */
    @FormUrlEncoded
    @POST("users/other/listVisitors")
    fun listVisitor(@Field("token") token: String): Single<Response<List<User>>>

    /**
     * 获取我关注的人列表
     */
    @FormUrlEncoded
    @POST("users/other/listCares")
    fun listFollow(@Field("token") token: String): Single<Response<List<User>>>

    @FormUrlEncoded
    @POST("users/other/care")
    fun follow(@Field("token") token: String,
               @Field("uuid") uuid: String): Single<Response<Boolean>>

    @FormUrlEncoded
    @POST("users/other/amountInfoOfUsers")
    fun getUserAmountInfo(@Field("token") token: String): Single<Response<AmountInfo>>

    /**
     * 发布约会
     *@param  token    string	是	用户会话token
     *@param  content    string	是	约会内容
     *@param  city    number	是	约会城市
     *@param  days    string	是	约会时长
     *@param  latitude    string	否	纬度（和常规颠倒）
     *@param  longitude    string	否	经度
     *@param  title    string	否	定位的逆地理位置，可选
     *@param  location    string	否	具体约会地点
     *@param  program    string	是	约会节目（接口返回，传字符串， eg:吃饭）
     */
    @FormUrlEncoded
    @POST("appointment/addAppointment")
    fun publishDating(@Field("token") token: String,
                      @Field("content") content: String,
                      @Field("city") city: String,
                      @Field("days") days: String,
                      @Field("latitude") latitude: String,
                      @Field("longitude") longitude: String,
                      @Field("title") title: String,
                      @Field("location") location: String,
                      @Field("grogram") program: String): Single<Response<List<User>>>

    /**
     * 取消约会
     */
    @FormUrlEncoded
    @POST("appointment/cancelAppointment")
    fun cancelDating(@Field("token") token: String,
                     @Field("uuid") uuid: String): Single<Response<String>>

    /**
     * 参加约会
     */
    @FormUrlEncoded
    @POST("appointment/attendAppointment")
    fun joinDating(@Field("token") token: String,
                   @Field("uuid") uuid: String): Single<Response<String>>

    /**
     * 查看约会
     */
    @FormUrlEncoded
    @POST("appointment/scanAppointment")
    fun viewDating(@Field("token") token: String,
                   @Field("uuid") uuid: String): Single<Response<String>>

    /**
     * 审核约会
     */
    @FormUrlEncoded
    @POST("appointment/auditAttendAppointment")
    fun reviewDating(@Field("token") token: String,
                     @Field("uuid") uuid: String,
                     @Field("state") state: String): Single<Response<String>>

    /**
     * 约会节目
     */
    @GET("users/other/listProgram")
    fun listDatingProgram(@Query("token") token: String,
                          @Query("uuid") uuid: String): Single<Response<List<DatingProgram>>>

    /**
     * 该用户发布的约会
     */
    @GET("appointment/listAppointmentsByUser")
    fun listDatingByUser(@Query("token") token: String,
                         @Query("uuid") uuid: String,
                         @Query("page") pageIndex: String): Single<Response<List<Dating>>>

    /**
     * 推荐约会
     */
    @GET("appointment/listAppointment")
    fun listRecommendDating(@Query("token") token: String,
                            @Query("page") pageIndex: String,
                            @Query("dateType") dateType: Int,
                            @Query("distanceType") distanceType: Int,
                            @Query("grogram") program: String,
                            @Query("longitude") longitude: String,
                            @Query("latitude") latitude: String): Single<Response<List<Dating>>>

    /**
     * 热门约会
     */
    @GET("appointment/listPopularAppointment")
    fun listPopularDating(@Query("token") token: String,
                          @Query("page") pageIndex: String): Single<Response<List<Dating>>>

    /**
     * 约会详情
     */
    @GET("appointment/detailAppointment")
    fun getDatingDetail(@Query("token") token: String,
                        @Query("uuid") uuid: String,
                        @Query("latitude") latitude: String,
                        @Query("longitude") longitude: String): Single<Response<List<Dating>>>

    /**
     * 参加的约会
     */
    @GET("appointment/listAppointmentAttend")
    fun listJoinedDating(@Query("token") token: String,
                         @Query("page") pageIndex: String): Single<Response<List<Dating>>>

    /**
     * 被申请的约会
     */
    @GET("appointment/listAttendedItem")
    fun listAppliedDating(@Query("token") token: String,
                          @Query("page") pageIndex: String): Single<Response<List<Dating>>>


    /**
     * @param password  MD5后的数字密码（32位）
     * @param privatePassword  倒叙密码后的MD5的数字密码（32位）
     */
    @FormUrlEncoded
    @POST("users/setPrivatePassword")
    fun setPrivatePassword(@Field("token") token: String,
                           @Field("password") password: String,
                           @Field("privatePassword") privatePassword: String): Single<Response<String>>

    /**
     * @param photoPermission string 权限值(0 全部公开 1 红包（只有单张） 2 解锁相册(全部) 3 需要申请查看 4 不可查看)
     * @param money string	金额（红包类型必填）
     * @param money string	照片id（红包照片必填）
     */
    @FormUrlEncoded
    @POST("users/setPhotoRight")
    fun setPhotoPermission(@Field("token") token: String,
                           @Field("photoRight") photoPermission: String,
                           @Field("money") money: String,
                           @Field("id") photoId: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("users/other/listCareer")
    fun listCareer(@Field("token") token: String): Single<Response<List<Career>>>

    @FormUrlEncoded
    @POST("users/other/listIntroduces")
    fun listPurpose(@Field("token") token: String): Single<Response<List<Purpose>>>

    @FormUrlEncoded
    @POST("report/listReportItems")
    fun listReportReasons(): Single<Response<List<ReportReason>>>

    @FormUrlEncoded
    @POST("users/homePage/home")
    fun getUserInfo(@Field("token") token: String,
                    @Field("uid") uid: String,    //	用户uuid
                    @Field("latitude") latitude: String,
                    @Field("longitude") longitude: String): Single<Response<User>>

    @FormUrlEncoded
    @POST("report/report")
    fun report(@Field("token") token: String,
               @Field("type") type: String,
               @Field("targetUid") targetUid: String,
               @Field("content") content: String): Single<Response<String>>

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