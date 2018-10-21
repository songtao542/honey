package com.snt.phoney.di.module

import android.arch.lifecycle.ViewModelProvider
import com.snt.phoney.di.ViewModelFactory
import com.snt.phoney.ui.dating.create.CreateDatingActivity
import com.snt.phoney.ui.dating.list.DatingListActivity
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.ui.nearby.NearbyActivity
import com.snt.phoney.ui.signin.SigninActivity
import com.snt.phoney.ui.signup.SignupActivity
import com.snt.phoney.ui.startup.StartupActivity
import com.snt.phoney.ui.wallet.WalletActivity
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
    internal abstract fun contributeSignupActivity(): SignupActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeCreateDatingActivity(): CreateDatingActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeDatingListActivity(): DatingListActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeWalletActivity(): WalletActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    internal abstract fun contributeNearbyActivity(): NearbyActivity
}