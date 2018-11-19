/*
 *
 * Copyright (C) 2018. Faraday&Future
 * All rights reserved.
 * PROPRIETARY AND CONFIDENTIAL.
 * NOTICE: All information contained herein is, and remains the property of Faraday&Future Inc.
 * The intellectual and technical concepts contained herein are proprietary to Faraday&Future Inc.
 * and may be covered by U.S. and Foreign Patents, patents in process, and are protected
 * by trade secret and copyright law. Dissemination of this code or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Faraday&Future Inc.
 * Access to the source code contained herein is hereby forbidden to anyone except current
 * Faraday&Future Inc. employees or others who have executed Confidentiality and
 *  Non-disclosure agreements covering such access.
 *
 *
 */

package com.snt.phoney.repository

import android.app.Application
import com.appmattus.layercache.Cache
import com.appmattus.layercache.MapCache
import com.appmattus.layercache.jsonSerializer
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.utils.cache.KeyValueDatabaseCache
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheRepositoryImpl @Inject constructor(application: Application) : CacheRepository {

    private val firstCache: Cache<String, Any> = MapCache().jsonSerializer()
    private val secondCache: Cache<String, Any> = KeyValueDatabaseCache(application).jsonSerializer()

    private var cache: Cache<String, Any>

    init {
        cache = firstCache.compose(secondCache)
    }


    override fun <T > get(key: String): T? {
        var returned: T? = null
        runBlocking {
            returned = cache.get(key) as? T
        }
        return returned
    }

    override fun <T > set(key: String, value: T) {
        value?.let {
            runBlocking {
                cache.set(key, value).await()
            }
        }
    }
}