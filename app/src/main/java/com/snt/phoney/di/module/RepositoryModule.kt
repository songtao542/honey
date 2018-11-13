package com.snt.phoney.di.module

import com.snt.phoney.domain.repository.CacheRepository
import com.snt.phoney.domain.repository.LocationRepository
import com.snt.phoney.domain.repository.UserCredentialRepository
import com.snt.phoney.repository.CacheRepositoryImpl
import com.snt.phoney.repository.LocationRepositoryImpl
import com.snt.phoney.repository.UserCredentialRepositoryImpl
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
}