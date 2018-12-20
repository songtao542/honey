package com.snt.phoney.domain.accessor

import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.snt.phoney.domain.model.WeiboUser

interface WeiboUserAccessor {
    fun getWeiboUser(): WeiboUser?
    fun setWeiboUser(user: WeiboUser?)

    fun getWeiboAccessToken(): Oauth2AccessToken?
    fun setWeiboAccessToken(accessToken: Oauth2AccessToken?)
}