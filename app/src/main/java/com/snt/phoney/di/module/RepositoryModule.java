package com.snt.phoney.di.module;

import com.snt.phoney.domain.repository.CacheRepository;
import com.snt.phoney.domain.repository.DatingRepository;
import com.snt.phoney.domain.repository.JMessageRepository;
import com.snt.phoney.domain.repository.LocationRepository;
import com.snt.phoney.domain.repository.OrderRepository;
import com.snt.phoney.domain.repository.ToolRepository;
import com.snt.phoney.domain.repository.UserRepository;
import com.snt.phoney.domain.repository.WeiboUserRepository;
import com.snt.phoney.domain.repository.WxUserRepository;
import com.snt.phoney.domain.repository.impl.CacheRepositoryImpl;
import com.snt.phoney.domain.repository.impl.DatingRepositoryImpl;
import com.snt.phoney.domain.repository.impl.JMessageRepositoryImpl;
import com.snt.phoney.domain.repository.impl.LocationRepositoryImpl;
import com.snt.phoney.domain.repository.impl.OrderRepositoryImpl;
import com.snt.phoney.domain.repository.impl.ToolRepositoryImpl;
import com.snt.phoney.domain.repository.impl.UserRepositoryImpl;
import com.snt.phoney.domain.repository.impl.WeiboUserRepositoryImpl;
import com.snt.phoney.domain.repository.impl.WxUserRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {
    @Binds
    @Singleton
    public abstract UserRepository bindUserCredentialRepository(UserRepositoryImpl repository);

    @Binds
    @Singleton
    public abstract CacheRepository bindCacheRepository(CacheRepositoryImpl repository);

    @Binds
    @Singleton
    public abstract LocationRepository bindLocationRepository(LocationRepositoryImpl repository);

    @Binds
    @Singleton
    public abstract WeiboUserRepository bindWeiboUserRepository(WeiboUserRepositoryImpl repository);

    @Binds
    @Singleton
    public abstract WxUserRepository bindWxUserRepository(WxUserRepositoryImpl repository);

    @Binds
    @Singleton
    public abstract ToolRepository bindToolRepository(ToolRepositoryImpl repository);

    @Binds
    @Singleton
    public abstract DatingRepository bindDatingRepository(DatingRepositoryImpl repository);

    @Binds
    @Singleton
    public abstract OrderRepository bindOrderRepository(OrderRepositoryImpl repository);

    @Binds
    @Singleton
    public abstract JMessageRepository bindJMessageRepository(JMessageRepositoryImpl repository);
}