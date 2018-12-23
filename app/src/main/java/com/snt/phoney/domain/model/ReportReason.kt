package com.snt.phoney.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportReason(var id: Int = 0,
                        var name: String? = null)


enum class ReportType(val value: Int) {
    USER(0),
    DATING(1);

    companion object {
        fun from(value: Int): ReportType {
            return when (value) {
                0 -> USER
                else -> DATING
            }
        }
    }
}