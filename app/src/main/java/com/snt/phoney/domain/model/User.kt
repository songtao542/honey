/*
 * Copyright (c) 2018. Faraday&Future
 * All rights reserved.
 * PROPRIETARY AND CONFIDENTIAL.
 * NOTICE: All information contained herein is, and remains the property of Faraday&Future Inc.
 * The intellectual and technical concepts contained herein are proprietary to Faraday&Future Inc.
 * and may be covered by U.S. and Foreign Patents, patents in process, and are protected
 * by trade secret and copyright law. Dissemination of this code or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Faraday&Future Inc.
 * Access to the source code contained herein is hereby forbidden to anyone except current
 * Faraday&Future Inc. employees or others who have executed Confidentiality and
 * Non-disclosure agreements covering such access.
 */

package com.snt.phoney.domain.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Serializable
@Entity(tableName = "User")
data class User(
        @PrimaryKey var id: String,
        var username: String? = null,
        var email: String? = null,
        var mobile: String? = null,
        var password: String? = null,
        @Optional var avatar: String? = null,
        @Optional var avatarUrl: String? = null,
        var activated: Boolean = false,
        var emailVerified: Boolean = false) : Parcelable

