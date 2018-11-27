package com.snt.phoney.domain.model

import com.google.gson.annotations.SerializedName

data class AmountInfo(
        @SerializedName(value = "count_visitor") var countVisitor: Int = 0,
        @SerializedName(value = "count_care") var countFollowed: Int = 0,
        @SerializedName(value = "count_appointment") var countDating: Int = 0
)