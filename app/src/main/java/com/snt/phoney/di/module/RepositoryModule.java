package com.snt.phoney.di.module;

import com.snt.phoney.domain.repository.CacheRepository;
import com.snt.phoney.domain.repository.LocationRepository;
import com.snt.phoney.domain.repository.UserCredentialRepository;
import com.snt.phoney.domain.repository.WeiboUserRepository;
import com.snt.phoney.domain.repository.WxUserRepository;
import com.snt.phoney.repository.CacheRepositoryImpl;
import com.snt.phoney.repository.LocationRepositoryImpl;
import com.snt.phoney.repository.UserCredentialRepositoryImpl;
import com.snt.phoney.repository.WeiboUserRepositoryImpl;
import com.snt.phoney.repository.WxUserRepositoryImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {
    @Binds
    public abstract UserCredentialRepository bindUserCredentialRepository(UserCredentialRepositoryImpl repository);

    @Binds
    public abstract CacheRepository bindCacheRepository(CacheRepositoryImpl repository);

    @Binds
    public abstract LocationRepository bindLocationRepository(LocationRepositoryImpl repository);

    @Binds
    public abstract WeiboUserRepository bindWeiboUserRepository(WeiboUserRepositoryImpl repository);

    @Binds
    public abstract WxUserRepository bindWxUserRepository(WxUserRepositoryImpl repository);
}