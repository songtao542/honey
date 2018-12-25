package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class WithdrawInfo(
        var id: Int = 0,//"id": 1,
        var uuid: String? = null,//"uuid": "uo2018121210525700bf9b1b6694bd8b",
        var state: Int = 0,//"state": 0,
        @SerializedName(value = "state_msg") var stateMessage: String? = null,//   "state_msg": "处理中",
        @SerializedName(value = "type_msg") var typeMessage: String? = null,//"type_msg": "支付宝提现",
        var type: Int = 0,//"type": 1,
        var money: Double = 0.0,//"money": 15,
        var openid: String? = null,//"openid": "支付宝账号 208****2392",
        @SerializedName(value = "ctime") var createTime: Long = 0,//"ctime": 1544583177404,
        var items: List<WithDrawItem>? = null //"items": [
)

data class WithDrawItem(
        var reason: String? = null,//  "reason": "系统触发",
        @SerializedName(value = "ctime") var createTime: Long = 0,//    "ctime": 1544583177404,
        var state: Int = 0,//    "state": 1,
        var title: String? = null//    "title": "发起提现申请"
)


