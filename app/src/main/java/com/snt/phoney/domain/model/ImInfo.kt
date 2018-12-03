package com.snt.phoney.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ImInfo(
        var password: String? = null,//":"ca66dbe9584065332f917846a670aeaf",
        @SerializedName(value = "userName") var username: String? = null,//":"imu2018112816521080723aa89d706b15"},
        @SerializedName(value = "nickName") var nickname: String? = null//":"淞",
) : Parcelable