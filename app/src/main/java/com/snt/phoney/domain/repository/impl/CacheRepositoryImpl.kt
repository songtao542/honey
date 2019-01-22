package com.snt.phoney.domain.repository.impl

import android.app.Application
import com.appmattus.layercache.Cache
import com.appmattus.layercache.MapCache
import com.appmattus.layercache.jsonSerializer
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.utils.cache.KeyValueDatabaseCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    /**
     * 会阻塞当前线程
     */
    override suspend fun <T : Any> get(key: String): T? {
        return cache.get(key).await()?.value as? T
    }

    override fun <T : Any> set(key: String, value: T?, callback: ((old: T?) -> Unit)?) {
        GlobalScope.launch(Dispatchers.IO) {
            val old = cache.get(key).await()?.value as? T
            if (value != null) {
                cache.set(key, Value(value)).await()
                callback?.invoke(old)
            } else {
                cache.evict(key).await()
            }
            callback?.invoke(old)
        }
    }

    override fun clear(key: String) {
        GlobalScope.launch(Dispatchers.IO) {
            cache.evict(key).await()
        }
    }

    @Serializable
    data class Value(@Serializable val value: Any)
}