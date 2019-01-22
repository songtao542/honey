package com.snt.phoney.domain.repository


interface CacheRepository {
    suspend fun <T : Any> get(key: String): T?
    fun <T : Any> set(key: String, value: T?, callback: ((old: T?) -> Unit)? = null)
    fun clear(key: String)
}