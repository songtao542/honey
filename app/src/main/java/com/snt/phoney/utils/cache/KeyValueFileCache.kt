package com.snt.phoney.utils.cache

import android.app.Application
import android.util.Log
import com.appmattus.layercache.Cache
import com.snt.phoney.extensions.TAG
import com.snt.phoney.provider.AppSettings
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class KeyValueFileCache constructor(val application: Application) : Cache<String, String> {
    override fun evict(key: String): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                AppSettings.putString(application, key, "")
                return@async
            }
        }
    }

    override fun evictAll(): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                Log.d(this@KeyValueFileCache.TAG, "Don't support evict all!")
                return@async
            }
        }
    }

    override fun get(key: String): Deferred<String?> {
        return runBlocking {
            async(Dispatchers.Default) {
                AppSettings.getString(application, key)
            }
        }
    }

    override fun set(key: String, value: String): Deferred<Unit> {
        return runBlocking {
            async(Dispatchers.Default) {
                AppSettings.putString(application, key, value)
                return@async
            }
        }
    }

}