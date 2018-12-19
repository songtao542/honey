package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class MibiWallet(
        @SerializedName(value = "intimateGold") var mibi: Int = 0,
        var rules: List<MibiRule>? = null
)

data class MibiRule(
        var uuid: String? = null,// "uuid": "pr20181113182346f660abec7a88c33c"
        var money: Int = 0,//  "money": 12,
        @SerializedName(value = "phoney") var mibi: Int = 0// "": 180,
)