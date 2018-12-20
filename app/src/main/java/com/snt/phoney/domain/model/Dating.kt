package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

data class Dating(
        var cover: List<Photo>? = null,//  "cover": [
        @SerializedName(value = "isCared") var care: Boolean = false,//  "isCared": false,
        @SerializedName(value = "userInfo") var user: User? = null,//   "user":
        @SerializedName(value = "ctime") var createTime: Long = 0,//  "ctime": 1542381337650,
        var state: Int = 0,//  "state": 2,
        var uuid: String? = null,//  "uuid": "a201811162315375b737eb9a223d591",
        var content: String? = null, // "content": "3455555555"

        var distance: Double = 0.0,//"distance": 64.69999999999999,
        var city: String? = null,//"city": "深圳市",
        @SerializedName(value = "grogram") var program: String? = null,//"grogram": "看电影",
        var isAttend: Boolean = false,//"isAttend": false,

        var location: String? = null,//"location": "广东省,深圳市, 福田区, 港中旅大厦",
        var startTime: Long = 0,//"startTime": 1542470400000,
        var endTime: Long = 0//"endTime": 1542556800000
) {
    fun photoUrls(): List<String> {
        return cover?.map {
            it.path ?: ""
        } ?: emptyList()
    }

    fun formatTime(): String {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return if (startTime > 0 && endTime > 0) {
            "${df.format(startTime)} ~ ${df.format(endTime)}"
        } else {
            ""
        }
    }

    fun remainingTime(): String {
        var second = (endTime - System.currentTimeMillis()) / 1000
        val days = second / 86400            //转换天数
        second %= 86400            //剩余秒数
        val hours = second / 3600            //转换小时
        second %= 3600                //剩余秒数
        val minutes = second / 60            //转换分钟
        second %= 60                //剩余秒数
        return if (days > 0) {
            "${days}天${hours}小时${minutes}分${second}秒"
        } else if (hours > 0) {
            "${hours}小时${minutes}分${second}秒"
        } else {
            "${minutes}分${second}秒"
        }
    }

}