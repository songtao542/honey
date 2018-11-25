package com.snt.phoney.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "User")
@Parcelize
@Serializable
data class User(
        @PrimaryKey var id: String = "",
        var uuid: String? = null,
        var username: String? = null,
        @SerializedName(value = "nickName") var nickname: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var password: String? = null,
        @Optional var avatar: String? = null,
        var sex: Int = -1,
        var height: Int = 0,
        var weight: Float = 0f,
        var age: Int = 0,
        var cup: String? = null,
        var city: String? = null,
        var career: String? = null,
        var portrait: String? = null,
        var introduce: String? = null,
        var photo: String? = null,
        var photoRight: String? = null,
        @SerializedName(value = "account_wx") var wechatAccount: String? = null,
        @SerializedName(value = "pauthentication") var verified: String? = null,
        var token: String? = null,
        var open: Int = 0,
        var price: Float = 0f,
        var state: Int = 0,
        var tag: String? = null,
        @SerializedName(value = "cares") var followedSize: Int = 0,
        @SerializedName(value = "utime") var updateTime: Long = 0) : Parcelable


enum class Sex(var value: Int) {
    MALE(0),
    FEMALE(1),
    UNKNOWN(-1);

    companion object {
        fun from(value: Int): Sex {
            return when (value) {
                0 -> MALE
                1 -> FEMALE
                else -> UNKNOWN
            }
        }
    }
}

