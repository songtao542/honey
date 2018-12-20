package com.snt.phoney.domain.repository

import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.snt.phoney.domain.accessor.WeiboUserAccessor
import com.snt.phoney.domain.model.WeiboUser
import io.reactivex.Single

interface WeiboUserRepository : WeiboUserAccessor {
    fun getUserInfo(accessToken: String, uid: String): Single<WeiboUser>

    fun refreshToken(): Single<Oauth2AccessToken>
}