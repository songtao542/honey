package com.snt.phoney.di.module;

import com.snt.phoney.domain.accessor.UserAccessor;
import com.snt.phoney.domain.accessor.WeiboUserAccessor;
import com.snt.phoney.domain.accessor.WxUserAccessor;
import com.snt.phoney.domain.accessor.impl.UserAccessorImpl;
import com.snt.phoney.domain.accessor.impl.WeiboUserAccessorImpl;
import com.snt.phoney.domain.accessor.impl.WxUserAccessorImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AccessorModule {

    @Binds
    @Singleton
    public abstract UserAccessor bindUserAccessor(UserAccessorImpl userAccessor);

    @Binds
    @Singleton
    public abstract WeiboUserAccessor bindWeiboUserAccessor(WeiboUserAccessorImpl weiboUserAccessor);

    @Binds
    @Singleton
    public abstract WxUserAccessor bindWxUserAccessor(WxUserAccessorImpl wxUserAccessor);

}
