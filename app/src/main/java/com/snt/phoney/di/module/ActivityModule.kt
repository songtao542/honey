package com.snt.phoney.di.module

import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.di.ViewModelFactory
import com.snt.phoney.ui.dating.DatingActivity
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.ui.nearby.NearbyActivity
import com.snt.phoney.ui.setup.SetupWizardActivity
import com.snt.phoney.ui.signup.SignupActivity
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.ui.vip.VipActivity
import com.snt.phoney.wxapi.WXEntryActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    //@SignupScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeSignupActivity(): SignupActivity

    //@SignupScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeWXEntryActivity(): WXEntryActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeSetupWizardActivity(): SetupWizardActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeCommonActivity(): CommonActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeNearbyActivity(): NearbyActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeVipActivity(): VipActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeUserInfoActivity(): UserActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeDatingActivity(): DatingActivity
}