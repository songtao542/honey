package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.repository.WeiboUserRepository
import javax.inject.Inject

class WeiboSigninUseCase @Inject constructor(private val repository: WeiboUserRepository) {

    fun getUserInfo(accessToken: String, uid: String) = repository.getUserInfo(accessToken, uid)

    fun refreshToken() = repository.refreshToken()

    var accessToken = repository.accessToken

}