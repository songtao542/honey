package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat

@Serializable
data class VipInfo(
        /**
         * 会员开始时间
         */
        var startTime: Long = 0,// "startTime": null,
        /**
         * 会员到期时间
         */
        var endTime: Long = 0,//"endTime": null,
        /**
         *  1 是会员， 0 普通会员
         */
        @SerializedName(value = "isMember") var vip: Int = 0//  "isMember": 0,
) {
    val isVip: Boolean
        get() = vip != 0

    fun formatEndTime(): String {
        val df = SimpleDateFormat("yyyy.MM.dd")
        return df.format(endTime)
    }
}



