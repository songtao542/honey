package com.snt.phoney.di.module

import com.snt.phoney.base.CommonActivity
import com.snt.phoney.di.ActivityScope
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.ui.setup.SetupWizardActivity
import com.snt.phoney.ui.signup.SignupActivity
import com.snt.phoney.wxapi.WXEntryActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        SignupActivityModule::class
    ])
    internal abstract fun contributeSignupActivity(): SignupActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        WXEntryActivityModule::class
    ])
    internal abstract fun contributeWXEntryActivity(): WXEntryActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        SetupWizardActivityModule::class
    ])
    internal abstract fun contributeSetupWizardActivity(): SetupWizardActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        MainActivityModule::class
    ])
    internal abstract fun contributeMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        CommonActivityModule::class
    ])
    internal abstract fun contributeCommonActivity(): CommonActivity

//    @ActivityScope
//    @ContributesAndroidInjector(modules = [FragmentModule::class, ViewModelModule::class])
//    internal abstract fun contributeNearbyActivity(): NearbyActivity
//
//    @ActivityScope
//    @ContributesAndroidInjector(modules = [FragmentModule::class, ViewModelModule::class])
//    internal abstract fun contributeVipActivity(): VipActivity
//
//    @ActivityScope
//    @ContributesAndroidInjector(modules = [FragmentModule::class, ViewModelModule::class])
//    internal abstract fun contributeUserInfoActivity(): UserActivity
//
//    @ActivityScope
//    @ContributesAndroidInjector(modules = [FragmentModule::class, ViewModelModule::class])
//    internal abstract fun contributeDatingActivity(): DatingActivity
}