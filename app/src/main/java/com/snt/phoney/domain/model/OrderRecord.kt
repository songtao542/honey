package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class OrderRecord(
        var uuid: String? = null,//"uuid": "uo20181201163756f5b309a2699d6928",
        var type: Int = 0,//"type": 0,
        var payType: String? = null,//   "payType": "微信支付",
        var title: String? = null,//"title": "蜜币充值",
        var price: String? = null,//"price": "-158.0元",
        var info: String? = null,//"info": "余额：10000",
        @SerializedName(value = "ctime") var createTime: Long = 0//"ctime": 1543653476374,
)