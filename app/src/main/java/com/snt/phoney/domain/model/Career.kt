package com.snt.phoney.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Career(var id: Long = 0,
                  var name: String? = null) {

    @Transient
    val safeName: String
        get() = name ?: ""

}