package com.snt.phoney.domain.repository.impl

import android.app.Application
import com.sina.weibo.sdk.auth.AccessTokenKeeper
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.exception.WeiboException
import com.sina.weibo.sdk.net.RequestListener
import com.snt.phoney.api.WeiboApi
import com.snt.phoney.domain.accessor.WeiboUserAccessor
import com.snt.phoney.domain.model.WeiboUser
import com.snt.phoney.domain.repository.WeiboUserRepository
import com.snt.phoney.utils.data.Constants
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeiboUserRepositoryImpl @Inject constructor(private val application: Application,
                                                  private val userAccessor: WeiboUserAccessor,
                                                  private val weiboApi: WeiboApi)
    : WeiboUserRepository, WeiboUserAccessor by userAccessor {

    override fun getUserInfo(accessToken: String, uid: String): Single<WeiboUser> {
        return weiboApi.getUserInfo(accessToken, uid)
    }

    override fun refreshToken(): Single<Oauth2AccessToken> {
        return Single.create<Oauth2AccessToken> { emitter ->
            AccessTokenKeeper.refreshToken(Constants.Weibo.APP_KEY, application, object : RequestListener {
                override fun onComplete(response: String) {
                    GlobalScope.launch(Dispatchers.Default) {
                        val token = getWeiboAccessToken()
                        if (token != null) {
                            emitter.onSuccess(token)
                        } else {
                            emitter.onError(Exception("Refresh token error"))
                        }
                    }
                }

                override fun onWeiboException(e: WeiboException) {
                    emitter.onError(e)
                }
            })
        }
    }

}

