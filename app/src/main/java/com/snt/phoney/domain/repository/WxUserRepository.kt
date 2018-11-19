package com.snt.phoney.domain.repository

import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import io.reactivex.Single

interface WxUserRepository {
    fun getAccessToken(code: String, grantType: String): Single<WxAccessToken>

    fun refreshToken(refreshToken: String, grantType: String): Single<WxAccessToken>

    fun getUserInfo(accessToken: String, openid: String): Single<WxUser>

    var accessToken: WxAccessToken?

    var user: WxUser?
}