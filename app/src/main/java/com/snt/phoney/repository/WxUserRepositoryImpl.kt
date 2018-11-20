package com.snt.phoney.repository

import com.snt.phoney.api.WxApi
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.domain.repository.WxUserRepository
import com.snt.phoney.utils.data.Constants
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WxUserRepositoryImpl @Inject constructor(private val cache: CacheRepository, private val wxapi: WxApi) : WxUserRepository {

    override fun getAccessToken(code: String, grantType: String): Single<WxAccessToken> {
        return wxapi.getAccessToken(code, grantType)
    }

    override fun refreshToken(refreshToken: String, grantType: String): Single<WxAccessToken> {
        return wxapi.refreshToken(refreshToken, grantType)
    }

    override fun getUserInfo(accessToken: String, openid: String): Single<WxUser> {
        return wxapi.getUserInfo(accessToken, openid)
    }

    override var accessToken: WxAccessToken?
        set(value) = cache.set(Constants.Cache.WX_ACCESS_TOKEN, value)
        get() = cache.get(Constants.Cache.WX_ACCESS_TOKEN)

    override var user: WxUser?
        set(value) = cache.set(Constants.Cache.WX_USER, value)
        get() = cache.get(Constants.Cache.WX_USER)

}

