package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class AuthState(
        @SerializedName(value = "msg") var message: String? = null,// "msg": "您还未通过纯蜜官方认证！"
        var score: String? = null,//"score": "您还未通过纯蜜官方真人验证，靠谱值为0",
        /**
         * 0 未认证  1 认证中  2 认证通过  3 认证未通过
         */
        var state: Int = 0//"state": 0
) {
    val isVerified: Boolean
        get() = state == 2
}

