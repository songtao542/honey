package com.snt.phoney.repository

import android.app.Application
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.exception.WeiboException
import com.sina.weibo.sdk.net.RequestListener
import com.snt.phoney.api.WeiboApi
import com.snt.phoney.domain.model.WeiboUser
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.domain.repository.WeiboUserRepository
import com.snt.phoney.utils.data.Constants
import io.reactivex.Single
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeiboUserRepositoryImpl @Inject constructor(private val application: Application, private val cache: CacheRepository, private val weiboApi: WeiboApi) : WeiboUserRepository {

    override fun getUserInfo(accessToken: String, uid: String): Single<WeiboUser> {
        return weiboApi.getUserInfo(accessToken, uid)
    }


    override fun refreshToken(): Single<Oauth2AccessToken> {
        return Single.create<Oauth2AccessToken> { emitter ->
            AccessTokenKeeper.refreshToken(Constants.Weibo.APP_KEY, application, object : RequestListener {
                override fun onComplete(response: String) {
                    //置为null，重新读取
                    _accessToken = null
                    val token = accessToken
                    if (token != null) {
                        emitter.onSuccess(token)
                    } else {
                        emitter.onError(Exception("Refresh token error"))
                    }
                }

                override fun onWeiboException(e: WeiboException) {
                    emitter.onError(e)
                }
            })
        }
    }

    private var _accessToken: Oauth2AccessToken? = null

    override var accessToken: Oauth2AccessToken?
        set(value) {
            runBlocking {
                async {
                    AccessTokenKeeper.writeAccessToken(application, value)
                }.await()
            }
        }
        get() {
            if (_accessToken == null) {
                runBlocking {
                    async {
                        _accessToken = AccessTokenKeeper.readAccessToken(application)
                    }.await()
                }
            }
            return _accessToken
        }

    override var user: WeiboUser?
        get() = cache.get(Constants.Cache.WEIBO_USER)
        set(value) = cache.set(Constants.Cache.WEIBO_USER, value)

}

