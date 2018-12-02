package com.snt.phoney.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Parcelize
@Serializable
data class DatingProgram(
        var id: Int = 0,
        var name: String? = null) : Parcelable {

    @Transient
    val safeName: String
        get() {
            return name ?: ""
        }
}