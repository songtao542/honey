package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PreWithdraw(
        @SerializedName(value = "frule") var rule: String? = null,//   "frule": "10蜜币等于人民币 0.50¥",
        @SerializedName(value = "flimit") var limit: String? = null,//"flimit": "每日限额1000元",
        var avaliable: String? = null,// "avaliable": "15.50",
        @SerializedName(value = "alipay_bind") var isAlipayBind: Boolean = false,//"alipay_bind": true,
        var info: String? = null//"info": "最低30，最高1000元（48小时到账）"
)