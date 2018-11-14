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
import com.snt.phoney.domain.model.User
import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.utils.cache.DatabaseCache
import com.snt.phoney.utils.cache.KeyValueDatabaseCache
import com.snt.phoney.utils.data.Constants
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.internal.defaultSerializer
import kotlinx.serialization.json.JSON
import kotlinx.serialization.serializer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheRepositoryImpl @Inject constructor(application: Application) : CacheRepository {

    private val userFirstCache: Cache<String, User> = MapCache().jsonSerializer()
    //private val userSecondCache: Cache<String, User> = SharedPreferencesCache(application, Constants.Cache.USER_CACHE_NAME).withString().jsonSerializer()
    private val userSecondCache: Cache<String, User> = KeyValueDatabaseCache(application).jsonSerializer()

    private var userCache: Cache<String, User>

    init {
        userCache = userFirstCache.compose(userSecondCache)
    }

    override var user: User?
        get() {
            var userReturned: User? = null
            runBlocking {
                userReturned = userCache.get(Constants.Cache.USER).await()
            }
            return userReturned
        }
        set(value) {
            value?.let {
                userCache.set(Constants.Cache.USER, value)
            }
        }
}