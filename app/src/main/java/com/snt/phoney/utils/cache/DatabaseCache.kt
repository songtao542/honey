package com.snt.phoney.utils.cache

import android.app.Application
import com.appmattus.layercache.Cache
import com.snt.phoney.domain.persistence.AppDatabase
import com.snt.phoney.domain.persistence.KeyValueDao
import com.snt.phoney.domain.persistence.put
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

//abstract class DatabaseCache<V : Any> constructor(application: Application) : Cache<String, V> {
//    private val keyValeDao: KeyValueDao = AppDatabase.getInstance(application).keyValueDao()
//
//    override fun evict(key: String): Deferred<Unit> {
//        return GlobalScope.async {
//            keyValeDao.remove(key)
//        }
//    }
//
//    override fun evictAll(): Deferred<Unit> {
//        return GlobalScope.async {
//            keyValeDao.removeAll()
//        }
//    }
//
//    override fun get(key: String): Deferred<V?> {
//        return GlobalScope.async {
//            keyValeDao.get(key)?.value?.let {
//                parse(it)
//            }
//        }
//    }
//
//    override fun set(key: String, value: V): Deferred<Unit> {
//        return GlobalScope.async {
//            keyValeDao.put(key, stringify(value))
//        }
//    }
//
//    abstract fun parse(value: String): V?
//
//    abstract fun stringify(value: V): String
//
//}

class KeyValueDatabaseCache constructor(application: Application) : Cache<String, String> {
    private val keyValeDao: KeyValueDao = AppDatabase.getInstance(application).keyValueDao()

    override fun evict(key: String): Deferred<Unit> {
        return GlobalScope.async {
            keyValeDao.remove(key)
        }
    }

    override fun evictAll(): Deferred<Unit> {
        return GlobalScope.async {
            keyValeDao.removeAll()
        }
    }

    override fun get(key: String): Deferred<String?> {
        return GlobalScope.async {
            keyValeDao.get(key)?.value
        }
    }

    override fun set(key: String, value: String): Deferred<Unit> {
        return GlobalScope.async {
            keyValeDao.put(key, value)
        }
    }
}