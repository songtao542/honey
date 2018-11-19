package com.snt.phoney.domain.repository

import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.snt.phoney.domain.model.WeiboUser
import io.reactivex.Single

interface WeiboUserRepository {
    fun getUserInfo(accessToken: String, uid: String): Single<WeiboUser>

    fun refreshToken(): Single<Oauth2AccessToken>

    var accessToken: Oauth2AccessToken?

    var user: WeiboUser?
}