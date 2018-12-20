package com.snt.phoney.di.module;

import com.snt.phoney.domain.repository.CacheRepository;
import com.snt.phoney.domain.repository.DatingRepository;
import com.snt.phoney.domain.repository.LocationRepository;
import com.snt.phoney.domain.repository.OrderRepository;
import com.snt.phoney.domain.repository.ToolRepository;
import com.snt.phoney.domain.repository.UserRepository;
import com.snt.phoney.domain.repository.WeiboUserRepository;
import com.snt.phoney.domain.repository.WxUserRepository;
import com.snt.phoney.domain.repository.impl.CacheRepositoryImpl;
import com.snt.phoney.domain.repository.impl.DatingRepositoryImpl;
import com.snt.phoney.domain.repository.impl.LocationRepositoryImpl;
import com.snt.phoney.domain.repository.impl.OrderRepositoryImpl;
import com.snt.phoney.domain.repository.impl.ToolRepositoryImpl;
import com.snt.phoney.domain.repository.impl.UserRepositoryImpl;
import com.snt.phoney.domain.repository.impl.WeiboUserRepositoryImpl;
import com.snt.phoney.domain.repository.impl.WxUserRepositoryImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {
    @Binds
    public abstract UserRepository bindUserCredentialRepository(UserRepositoryImpl repository);

    @Binds
    public abstract CacheRepository bindCacheRepository(CacheRepositoryImpl repository);

    @Binds
    public abstract LocationRepository bindLocationRepository(LocationRepositoryImpl repository);

    @Binds
    public abstract WeiboUserRepository bindWeiboUserRepository(WeiboUserRepositoryImpl repository);

    @Binds
    public abstract WxUserRepository bindWxUserRepository(WxUserRepositoryImpl repository);

    @Binds
    public abstract ToolRepository bindToolRepository(ToolRepositoryImpl repository);

    @Binds
    public abstract DatingRepository bindDatingRepository(DatingRepositoryImpl repository);

    @Binds
    public abstract OrderRepository bindOrderRepository(OrderRepositoryImpl repository);
}