package com.snt.phoney.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ImUser(
        var id: Long = 0,
        var password: String? = null,//":"ca66dbe9584065332f917846a670aeaf",
        @SerializedName(value = "userName") var username: String? = null,//":"imu2018112816521080723aa89d706b15"},
        @SerializedName(value = "nickName") var nickname: String? = null,//":"淞",
        var avatar: String? = null//":"淞",
) : Parcelable {

//    companion object {
//        @JvmStatic
//        fun from(user: cn.jpush.im.android.api.model.UserInfo): ImUser {
//            val id = user.userID
//            val nickname = user.nickname
//            val username = user.userName
//            val avatar = user.avatar
//            return ImUser(id = id, username = username, nickname = nickname, avatar = avatar)
//        }
//
//        @JvmStatic
//        fun from(user: User): ImUser? {
//            val imUser = user.im
//            imUser?.avatar = user.avatar
//            return imUser
//        }
//    }
}