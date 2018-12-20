package com.snt.phoney.domain.repository

import com.snt.phoney.domain.accessor.WxUserAccessor
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import io.reactivex.Single

interface WxUserRepository : WxUserAccessor {
    fun getAccessToken(code: String, grantType: String): Single<WxAccessToken>

    fun refreshToken(refreshToken: String, grantType: String): Single<WxAccessToken>

    fun getUserInfo(accessToken: String, openid: String): Single<WxUser>
}