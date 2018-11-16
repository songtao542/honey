package com.snt.phoney.api

import com.snt.phoney.domain.model.WeiboUser
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface WeiboApi {

    @GET("2/users/show.json")
    fun getUserInfo(@Query("access_token") accessToken: String, @Query("uid") uid: String): Single<WeiboUser>

}