package com.snt.phoney.di.module

import com.snt.phoney.domain.repository.*
import com.snt.phoney.repository.*
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserCredentialRepository(repository: UserCredentialRepositoryImpl): UserCredentialRepository

    @Binds
    abstract fun bindCacheRepository(repository: CacheRepositoryImpl): CacheRepository

    @Binds
    abstract fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @Binds
    abstract fun bindWeiboUserRepository(repository: WeiboUserRepositoryImpl): WeiboUserRepository

    @Binds
    abstract fun bindWxUserRepository(repository: WxUserRepositoryImpl): WxUserRepository
}