package com.snt.phoney.domain.usecase

import com.snt.phoney.domain.accessor.WxUserAccessor
import com.snt.phoney.domain.repository.WxUserRepository
import javax.inject.Inject

class WxSigninUseCase @Inject constructor(private val repository: WxUserRepository) : WxUserAccessor by repository {

    fun getAccessToken(code: String, grantType: String) = repository.getAccessToken(code, grantType)

    fun refreshToken(refreshToken: String, grantType: String) = repository.refreshToken(refreshToken, grantType)

    fun getUserInfo(accessToken: String, openid: String) = repository.getUserInfo(accessToken, openid)

}