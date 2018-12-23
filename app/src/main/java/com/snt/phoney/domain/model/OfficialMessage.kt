package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class OfficialMessage(
        var uuid: String? = null,//"uuid": "m2018120413455836f184d730c1dad0",,,,,
        var cover: String? = null,//    "cover": "http://phoney.alance.pub/phoney/phoney/512/message/cover/1543919621909-0-4e45bea8293cf3884c7d9f6359f77037.jpg",
        var title: String? = null,//"title": "今日发布啦",
        var subTitle: String? = null,// "subTitle": "发布啦2222",
        @SerializedName(value = "ctime") var crateTime: Long = 0,//"ctime": 1543902358275,
        @SerializedName(value = "ntype") var type: Int = 0,//"ntype": 0,
        @SerializedName(value = "url") var url: String? = null//"ntype": 0,
)