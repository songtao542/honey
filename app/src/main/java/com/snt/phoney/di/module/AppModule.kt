package com.snt.phoney.di.module

import dagger.Module

@Module(includes = [
    ActivityModule::class,
//    FragmentModule::class,
//    ViewModelModule::class,
    RestApiServiceModule::class,
    RepositoryModule::class,
    DaoModule::class,
    WeiboApiServiceModule::class,
    WxApiServiceModule::class
])
class AppModule