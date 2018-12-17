package com.snt.phoney.repository

import android.app.Application
import com.appmattus.layercache.Cache
import com.appmattus.layercache.MapCache
import com.appmattus.layercache.jsonSerializer
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.utils.cache.KeyValueDatabaseCache
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheRepositoryImpl @Inject constructor(application: Application) : CacheRepository {

    private val firstCache: Cache<String, Value> = MapCache().jsonSerializer()
    private val secondCache: Cache<String, Value> = KeyValueDatabaseCache(application).jsonSerializer()

    private var cache: Cache<String, Value>

    init {
        cache = firstCache.compose(secondCache)
    }

    override fun <T : Any> get(key: String): T? {
        var result: T? = null
        runBlocking {
            result = cache.get(key).await()?.value as? T
        }
        return result
    }

    override fun <T : Any> set(key: String, value: T?) {
        if (value != null) {
            runBlocking {
                cache.set(key, Value(value)).await()
            }
        } else {
            runBlocking {
                cache.evict(key).await()
            }
        }
    }

    override fun clear(key: String) {
        runBlocking {
            cache.evict(key).await()
        }
    }

    @Serializable
    data class Value(@Serializable val value: Any)
}