package com.snt.phoney.di.module;

import android.app.Application;

import com.snt.phoney.domain.persistence.AppDatabase;
import com.snt.phoney.domain.persistence.KeyValueDao;
import com.snt.phoney.domain.persistence.PhotoDao;
import com.snt.phoney.domain.persistence.ProvinceCityDao;
import com.snt.phoney.domain.persistence.UserDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {
    @Singleton
    @Provides
    public static UserDao provideUserDao(AppDatabase database) {
        return database.userDao();
    }

    @Singleton
    @Provides
    public static KeyValueDao provideKeyValueDao(AppDatabase database) {
        return database.keyValueDao();
    }

    @Singleton
    @Provides
    public static ProvinceCityDao provideProvinceCityDao(AppDatabase database) {
        return database.provinceCityDao();
    }

    @Singleton
    @Provides
    public static PhotoDao providePhotoDao(AppDatabase database) {
        return database.photoDao();
    }

    @Singleton
    @Provides
    public static AppDatabase provideAppDatabase(Application application) {
        return AppDatabase.Companion.getInstance(application);
    }


}