package com.snt.phoney.domain.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Province", indices = [Index(value = ["id"], unique = true)])
data class Province(@PrimaryKey var id: Int = 0,
                    var name: String? = null,
                    @SerializedName(value = "items") @Ignore var cities: List<City>? = null)

