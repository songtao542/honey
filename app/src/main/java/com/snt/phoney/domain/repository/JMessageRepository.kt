package com.snt.phoney.domain.repository

interface JMessageRepository {
    fun login(username: String, password: String, callback: ((responseCode: Int, responseMessage: String) -> Unit)? = null)

    fun logout()
}