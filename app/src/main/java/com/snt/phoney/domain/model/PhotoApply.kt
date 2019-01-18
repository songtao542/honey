package com.snt.phoney.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.text.SimpleDateFormat

@Parcelize
@Serializable
data class PhotoApply(
        /**
         * 用户 uuid
         */
        var uid: String? = null,//"uuid": "u20181204173309703e6c143d35a6a7",
        var uuid: String? = null,//"uuid": "u20181204173309703e6c143d35a6a7",
        @SerializedName(value = "nickName") var nickname: String? = null,//  "nickName": "淞",
        var sex: Int = 0,//"sex": 1,
        var age: Int = 0,//"age": null,
        var height: Int = 0,//"height": null,
        var weight: Double = 0.0,//"weight": null,
        var cup: String? = null,//"cup": null
        /**
         * 0 为审核  1 同意  2 拒绝  10 失效
         */
        var state: Int = 0,//"state": 0,
        @SerializedName(value = "portrait") var avatar: String? = null,//"portrait": "http://phoney.alance.pub/phoney/phoney/512/users/cover/6e1b51f15a51906faf2708fc9104cb9c.jpg",
        @SerializedName(value = "ctime") var createTime: Long = 0 //"ctime": 1543915989895,
) : Parcelable {

    @Transient
    val safeUuid: String
        get() = uuid ?: ""

    fun formatCreateTime(): String {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return df.format(createTime)
    }
}