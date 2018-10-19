package com.snt.phoney.di.module

import com.snt.phoney.ui.fragment.LoginFragment
import com.snt.phoney.ui.fragment.ResetPasswordFragment
import com.snt.phoney.ui.fragment.SettingsFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeResetPasswordFragment(): ResetPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment


}