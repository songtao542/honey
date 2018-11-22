package com.snt.phoney.di.module;

import android.app.Application;

import com.snt.phoney.domain.persistence.AppDatabase;
import com.snt.phoney.domain.persistence.KeyValueDao;
import com.snt.phoney.domain.persistence.ProvinceCityDao;
import com.snt.phoney.domain.persistence.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {
    @Singleton
    @Provides
    public static UserDao provideUserDao(Application application) {
        return AppDatabase.Companion.getInstance(application).userDao();
    }

    @Singleton
    @Provides
    public static KeyValueDao provideKeyValueDao(Application application) {
        return AppDatabase.Companion.getInstance(application).keyValueDao();
    }

    @Singleton
    @Provides
    public static ProvinceCityDao provideProvinceCityDao(Application application) {
        return AppDatabase.Companion.getInstance(application).provinceCityDao();
    }


    @Singleton
    @Provides
    public static AppDatabase provideAppDatabase(Application application) {
        return AppDatabase.Companion.getInstance(application);
    }


}