package com.snt.phoney.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "User")
@Parcelize
@Serializable
data class User(
        @PrimaryKey var id: String = "",
        var username: String? = null,
        var email: String? = null,
        var mobile: String? = null,
        var password: String? = null,
        @Optional var avatar: String? = null,
        @Optional var avatarUrl: String? = null,
        var activated: Boolean = false,
        var emailVerified: Boolean = false,
        var sex: Int? = 0,
        var height: Int? = 0,
        var weight: Float? = 0f,
        var age: Int? = 0,
        var cup: String? = null,
        var birthYear: Int? = 0,
        var birthday: Long? = 0,
        var city: String? = null,
        var job: String? = null) : Parcelable

