package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

data class News(
        var uuid: String? = null,//"uuid": "m201901041009233f2607073100c31f",
        var url: String? = null,//"url": "https://api.chunmi69.com/news/news/m201901041009233f2607073100c31f",,,,
        var title: String? = null,// "title": "揭秘嫦娥四号月背之旅：攻克三大挑战终抵\"蟾宫后院\"",
        var cover: String? = null,//   "cover": "https://api.chunmi69.com/phoney/phoney/512/news/cover/1546567763964-0-842186299ed4562ee2ce07f4868e0f66.jpeg",
        var type: Int = 0,//"type": 1,,
        @SerializedName(value = "ctime") var createTime: Long = 0//"ctime": 1546567763952,
) {
    fun formatCreateTime(): String {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.format(createTime)
    }
}