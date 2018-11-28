package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class Dating(
        var cover: List<Photo>? = null,//  "cover": [
        @SerializedName(value = "isCared") var care: Boolean = false,//  "isCared": false,
        @SerializedName(value = "userInfo") var user: User? = null,//   "user":
        @SerializedName(value = "ctime") var createTime: Long = 0,//  "ctime": 1542381337650,
        var state: Int = 0,//  "state": 2,
        var uuid: String? = null,//  "uuid": "a201811162315375b737eb9a223d591",
        var content: String? = null  // "content": "3455555555"
) {
    fun photoUrls(): List<String> {
        return cover?.map {
            it.path ?: ""
        } ?: emptyList()
    }
}