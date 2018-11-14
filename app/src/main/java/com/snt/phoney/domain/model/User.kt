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
        var sex: Int? = -1,
        var height: Int? = 0,
        var weight: Float? = 0f,
        var age: Int? = 0,
        var cup: String? = null,
        var city: String? = null,
        var career: String? = null,
        var portrait: String? = null,
        var introduce: String? = null,
        var photo: String? = null,
        var photoRight: String? = null,
        @SerializedName(value = "account_wx") var wechatAccount: String? = null,
        var pauthentication: String? = null,
        var token: String? = null,
        var open: Int = 0,
        var price: Float = 0f,
        var state: Int = 0) : Parcelable


//"career": null,
//"nickName": "纯蜜nrskpc",
//"introduce": null,
//"sex": -1,
//"photoRight": 0,
//"weight": null,
//"photo": null,
//"portrait": null,
//"uuid": "u201811081829582566215b5a5a3b4f",
//"account_wx": null,
//"pauthentication": null,
//"token": "ebe02e1475ceed3abc0827459b59cec7",
//"isOpen": 0,
//"price": null,
//"id": 14,
//"state": 0,
//"age": null,
//"height": null,
//"cup": null

