package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class VipCombo(
        var uuid: String? = null,//"uuid": "mc2018111410284561ba1deedcb34657"
        var number: Double = 0.0,//  "number": 0.5,
        @SerializedName(value = "isRecommend") var recommend: Int = 0,//  "isRecommend": 0,
        var price: Double = 0.0,//"price": 240,
        var type: Int = 0// "type": 0,
) {

    val safeUuid: String
        get() = uuid ?: ""

    val isRecommend: Boolean
        get() = recommend != 0

}