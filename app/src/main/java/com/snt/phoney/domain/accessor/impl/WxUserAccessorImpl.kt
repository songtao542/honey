package com.snt.phoney.domain.accessor.impl

import com.snt.phoney.domain.accessor.WxUserAccessor
import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.utils.data.Constants
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class WxUserAccessorImpl @Inject constructor(private val cache: CacheRepository) : WxUserAccessor {
    override fun getWxUser(): WxUser? {
        var user: WxUser? = null
        runBlocking {
            user = cache.get(Constants.Cache.WX_USER)
        }
        return user
    }

    override fun setWxUser(user: WxUser?) {
        cache.set(Constants.Cache.WX_USER, user)
    }

    override fun getWxAccessToken(): WxAccessToken? {
        var accessToken: WxAccessToken? = null
        runBlocking {
            accessToken = cache.get(Constants.Cache.WX_ACCESS_TOKEN)
        }
        return accessToken
    }

    override fun setWxAccessToken(accessToken: WxAccessToken?) {
        cache.set(Constants.Cache.WX_ACCESS_TOKEN, accessToken)
    }
}