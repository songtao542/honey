package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Province(var id: Int = 0,
                    var name: String? = null,
                    @SerializedName(value = "items") var cities: ArrayList<City>? = null)

