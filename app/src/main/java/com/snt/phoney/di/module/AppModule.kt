package com.snt.phoney.di.module

import dagger.Module

@Module(includes = [ActivityModule::class, FragmentModule::class, RestApiServiceModule::class, ViewModelModule::class, RepositoryModule::class, DaoModule::class])
class AppModule