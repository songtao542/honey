package com.snt.phoney.utils.cache

import android.app.Application
import com.snt.phoney.domain.persistence.AppDatabase
import com.snt.phoney.domain.persistence.KeyValueDao
import com.snt.phoney.domain.persistence.put
import com.appmattus.layercache.Cache

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

abstract class DatabaseCache<V : Any> constructor(application: Application) : Cache<String, V> {
    private val keyValeDao: KeyValueDao = AppDatabase.getInstance(application).keyValueDao()

    override fun evict(key: String): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                keyValeDao.remove(key)
            }
        }
    }

    override fun evictAll(): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                keyValeDao.removeAll()
            }
        }
    }

    override fun get(key: String): Deferred<V?> {
        return runBlocking {
            async(Dispatchers.Default) {
                keyValeDao.get(key)?.value?.let {
                    parse(it)
                }
            }
        }
    }

    override fun set(key: String, value: V): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                keyValeDao.put(key, stringify(value))
            }
        }
    }

    abstract fun parse(value: String): V?

    abstract fun stringify(value: V): String


}

class KeyValueDatabaseCache constructor(application: Application) : Cache<String, String> {
    private val keyValeDao: KeyValueDao = AppDatabase.getInstance(application).keyValueDao()

    override fun evict(key: String): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                keyValeDao.remove(key)
            }
        }
    }

    override fun evictAll(): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                keyValeDao.removeAll()
            }
        }
    }

    override fun get(key: String): Deferred<String?> {
        return runBlocking {
            async(Dispatchers.Default) {
                keyValeDao.get(key)?.value
            }
        }
    }

    override fun set(key: String, value: String): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                keyValeDao.put(key, value)
            }
        }
    }


}