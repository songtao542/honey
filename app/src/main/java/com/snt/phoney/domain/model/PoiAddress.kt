package com.snt.phoney.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class PoiAddress(
        var title: String? = null,
        var distance: Double = 0.0,
        var position: Position? = null,
        var entrancePosition: Position? = null,
        var exitPosition: Position? = null,
        var tel: String? = null,
        var country: String? = null,
        var countryCode: String = "CN",
        var province: String? = null,
        var district: String? = null,
        var city: String? = null,
        var street: String? = null,
        var streetNumber: String? = null,
        var postalCode: String? = null,
        var address: String? = null
) : Parcelable {

    val latitude: Double
        get() = position?.latitude ?: 0.0

    val longitude: Double
        get() = position?.longitude ?: 0.0

    val formatAddress: String
        get() {
            return if (address != null) {
                address!!
            } else {
                "${province ?: ""}${city ?: ""}${district ?: ""}${street ?: ""}"
            }
        }
}

@Serializable
@Parcelize
data class Position(
        var latitude: Double = 0.0,
        var longitude: Double = 0.0
) : Parcelable