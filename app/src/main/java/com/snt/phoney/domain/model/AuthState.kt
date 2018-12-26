package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class AuthState(
        @SerializedName(value = "msg") var message: String? = null,// "msg": "未认证",
        var state: Int = 0//"state": 0
)