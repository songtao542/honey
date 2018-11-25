package com.snt.phoney.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportReason(var id: Int = 0,
                        var name: String? = null)