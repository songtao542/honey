package com.snt.phoney.api

import com.google.gson.JsonObject
import com.snt.phoney.domain.model.WeiboUser
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface WxApi {

    @GET("sns/oauth2/access_token")
    fun getAccessToken(
            //@Query("appid") appid: String,
            //@Query("secret") secret: String,
            @Query("code") code: String,
            @Query("grant_type") grantType: String
    ): Single<WxAccessToken>

    @GET("sns/oauth2/refresh_token")
    fun refreshToken(
            //@Query("appid") appid: String,
            @Query("refresh_token") refreshToken: String,
            @Query("grant_type") grantType: String
    ): Single<WxAccessToken>


    @GET("/sns/userinfo")
    fun getUserInfo(
            //@Query("appid") appid: String,
            @Query("access_token") accessToken: String,
            @Query("openid") openid: String
    ): Single<WxUser>


}