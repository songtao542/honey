package com.snt.phoney.di.module

import android.arch.lifecycle.ViewModelProvider
import com.snt.phoney.ui.activity.MainActivity
import com.snt.phoney.ui.activity.SigninActivity
import com.snt.phoney.ui.activity.StartupActivity
import com.snt.phoney.di.ViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector()
    internal abstract fun contributeStartupActivity(): StartupActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeSigninActivity(): SigninActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}