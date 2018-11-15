package com.snt.phoney.di.module

import android.app.Application
import com.snt.phoney.domain.persistence.AppDatabase
import com.snt.phoney.domain.persistence.KeyValueDao
import com.snt.phoney.domain.persistence.ProvinceCityDao
import com.snt.phoney.domain.persistence.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DaoModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideUserDao(application: Application): UserDao {
        return AppDatabase.getInstance(application).userDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideKeyValueDao(application: Application): KeyValueDao {
        return AppDatabase.getInstance(application).keyValueDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideProvinceCityDao(application: Application): ProvinceCityDao {
        return AppDatabase.getInstance(application).provinceCityDao()
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase {
        return AppDatabase.getInstance(application)
    }


}