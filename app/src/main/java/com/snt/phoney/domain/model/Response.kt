package com.snt.phoney.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(val data: T? = null, val code: Int = 0, val message: String? = null)


