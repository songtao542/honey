package com.snt.phoney.domain.accessor

import com.snt.phoney.domain.model.WxAccessToken
import com.snt.phoney.domain.model.WxUser

interface WxUserAccessor {
    fun getWxUser(): WxUser?
    fun setWxUser(user: WxUser?)

    fun getWxAccessToken(): WxAccessToken?
    fun setWxAccessToken(accessToken: WxAccessToken?)
}