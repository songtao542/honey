package com.snt.phoney.repository

import android.app.Application
import com.appmattus.layercache.Cache
import com.appmattus.layercache.MapCache
import com.appmattus.layercache.jsonSerializer
import com.snt.phoney.api.WxApi
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import com.snt.phoney.domain.repository.WxUserRepository
import com.snt.phoney.utils.cache.KeyValueDatabaseCache
import com.snt.phoney.utils.data.Constants
import io.reactivex.Single
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WxUserRepositoryImpl @Inject constructor(private val application: Application, private val wxapi: WxApi) : WxUserRepository {

    override fun getAccessToken(code: String, grantType: String): Single<WxAccessToken> {
        return wxapi.getAccessToken(code, grantType)
    }

    override fun refreshToken(refreshToken: String, grantType: String): Single<WxAccessToken> {
        return wxapi.refreshToken(refreshToken, grantType)
    }

    override fun getUserInfo(accessToken: String, openid: String): Single<WxUser> {
        return wxapi.getUserInfo(accessToken, openid)
    }

    private val accessTokenFirstCache: Cache<String, WxAccessToken> = MapCache().jsonSerializer()
    private val accessTokenSecondCache: Cache<String, WxAccessToken> = KeyValueDatabaseCache(application).jsonSerializer()
    private var accessTokenCache: Cache<String, WxAccessToken>

    init {
        accessTokenCache = accessTokenFirstCache.compose(accessTokenSecondCache)
    }

    @Suppress("DeferredResultUnused")
    override var accessToken: WxAccessToken?
        set(value) {
            value?.let {
                accessTokenCache.set(Constants.Cache.WX_ACCESS_TOKEN, value)
            }
        }
        get() {
            var result: WxAccessToken? = null
            runBlocking {
                result = accessTokenCache.get(Constants.Cache.WX_ACCESS_TOKEN).await()
            }
            return result
        }
}

