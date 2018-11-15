package com.snt.phoney.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Province")
data class Province(@PrimaryKey var id: Int = 0,
                    var name: String? = null,
                    @SerializedName(value = "items") var cities: List<City>? = null)

