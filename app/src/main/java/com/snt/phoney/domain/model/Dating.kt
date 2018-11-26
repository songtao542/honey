package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class Dating(
        var cover: List<PhotoInfo>,//  "cover": [
        @SerializedName(value = "isCared") var care: Boolean,//  "isCared": false,
        var userInfo: UserInfo,//   "userInfo":
        @SerializedName(value = "ctime") var createTime: Long,//  "ctime": 1542381337650,
        var state: Int,//  "state": 2,
        var uuid: String,//  "uuid": "a201811162315375b737eb9a223d591",
        var content: String  // "content": "3455555555"
) {
    fun photoUrls(): List<String> {
        return cover.map {
            it.path
        }
    }
}