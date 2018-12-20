package com.snt.phoney.domain.repository.impl

import com.snt.phoney.api.WxApi
import com.snt.phoney.domain.accessor.WxUserAccessor
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import com.snt.phoney.domain.repository.WxUserRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WxUserRepositoryImpl @Inject constructor(private val wxUserAccessor: WxUserAccessor,
                                               private val wxapi: WxApi)
    : WxUserRepository, WxUserAccessor by wxUserAccessor {

    override fun getAccessToken(code: String, grantType: String): Single<WxAccessToken> {
        return wxapi.getAccessToken(code, grantType)
    }

    override fun refreshToken(refreshToken: String, grantType: String): Single<WxAccessToken> {
        return wxapi.refreshToken(refreshToken, grantType)
    }

    override fun getUserInfo(accessToken: String, openid: String): Single<WxUser> {
        return wxapi.getUserInfo(accessToken, openid)
    }

}

