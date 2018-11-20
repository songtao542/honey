package com.snt.phoney.domain.usecase

import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.snt.phoney.domain.repository.WeiboUserRepository
import javax.inject.Inject

class WeiboSigninUseCase @Inject constructor(private val repository: WeiboUserRepository) {

    fun getUserInfo(accessToken: String, uid: String) = repository.getUserInfo(accessToken, uid)

    fun refreshToken() = repository.refreshToken()

    var accessToken: Oauth2AccessToken?
        set(value) {
            repository.accessToken = value
        }
        get() {
            return repository.accessToken
        }

}