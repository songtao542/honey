package com.snt.phoney.di.module;

import dagger.Module;

@Module(includes = {
        ActivityContributorModule.class,
        RestApiServiceModule.class,
        RepositoryModule.class,
        DaoModule.class,
        WeiboApiServiceModule.class,
        WxApiServiceModule.class
})
public abstract class AppModule {
}