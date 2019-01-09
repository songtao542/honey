package com.snt.phoney.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        ActivityContributorModule.class,
        RestApiServiceModule.class,
        AccessorModule.class,
        RepositoryModule.class,
        DaoModule.class,
        WeiboApiServiceModule.class,
        WxApiServiceModule.class
})
public abstract class AppModule {

    @Singleton
    @Provides
    public static Context provideContext(Application application) {
        return application;
    }

}