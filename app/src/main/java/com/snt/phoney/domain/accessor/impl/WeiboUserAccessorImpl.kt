package com.snt.phoney.domain.accessor.impl

import android.app.Application
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.snt.phoney.domain.accessor.WeiboUserAccessor
import com.snt.phoney.domain.model.WeiboUser
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.utils.data.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class WeiboUserAccessorImpl @Inject constructor(private val application: Application, private val cache: CacheRepository) : WeiboUserAccessor {
    override fun getWeiboUser(): WeiboUser? {
        var user: WeiboUser? = null
        runBlocking {
            user = cache.get(Constants.Cache.WEIBO_USER)
        }
        return user
    }

    override fun setWeiboUser(user: WeiboUser?) {
        cache.set(Constants.Cache.WEIBO_USER, user)
    }

    override fun getWeiboAccessToken(): Oauth2AccessToken? {
        var accessToken: Oauth2AccessToken? = null
        runBlocking {
            accessToken = AccessTokenKeeper.readAccessToken(application)
        }
        return accessToken
    }

    override fun setWeiboAccessToken(accessToken: Oauth2AccessToken?) {
        GlobalScope.launch(Dispatchers.IO) {
            AccessTokenKeeper.writeAccessToken(application, accessToken)
        }
    }
}