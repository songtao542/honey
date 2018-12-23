package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Applicant(
        /**
         * "nickName ,introduce,portrait,uuid"
         */
        @SerializedName(value = "userInfo") var user: User? = null,// "userInfo": {
        @SerializedName(value = "appointmentUuid") var datingId: String? = null,//  "appointmentUuid": "a2018112122393393b32fb96cde44d4",
        @SerializedName(value = "appointmentItemUuid") var uuid: String? = null,// "appointmentItemUuid": "ai201811222319267ca3685594bdec2b",
        @SerializedName(value = "ctime") var createTime: Long = 0,//  "ctime": 1542899966149,
        /**
         *  0 审核中  1 通过  2 拒绝   3 取消
         */
        var state: Int = 0//  "state": 0      // 0 审核中  1 通过  2 拒绝   3 取消
) {

    @Transient
    val safeUuid: String
        get() = uuid ?: ""

}