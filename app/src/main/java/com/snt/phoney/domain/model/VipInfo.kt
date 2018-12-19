package com.snt.phoney.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VipInfo(
        var startTime: Long = 0,// "startTime": null,
        var endTime: Long = 0,//"endTime": null,
        var vip: Int = 0//  "isMember": 0,
) {
    val isVip: Boolean
        get() = vip != 0
}



