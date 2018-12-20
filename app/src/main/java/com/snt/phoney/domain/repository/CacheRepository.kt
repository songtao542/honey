package com.snt.phoney.domain.repository


interface CacheRepository {
    suspend fun <T : Any> get(key: String): T?
    fun <T : Any> set(key: String, value: T?)
    fun clear(key: String)
}