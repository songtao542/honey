package com.snt.phoney.di.module

import com.snt.phoney.ui.home.HomeFragment
import com.snt.phoney.ui.message.MessageFragment
import com.snt.phoney.ui.mine.MineFragment
import com.snt.phoney.ui.dating.create.CreateDatingFragment
import com.snt.phoney.ui.dating.list.DatingListFragment
import com.snt.phoney.ui.nearby.NearbyFragment
import com.snt.phoney.ui.signin.SigninFragment
import com.snt.phoney.ui.resetpassword.ResetPasswordFragment
import com.snt.phoney.ui.settings.SettingsFragment
import com.snt.phoney.ui.signup.SignupFragment
import com.snt.phoney.ui.square.SquareFragment
import com.snt.phoney.ui.wallet.WalletFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeSigninFragment(): SigninFragment

    @ContributesAndroidInjector
    abstract fun contributeSignupFragment(): SignupFragment

    @ContributesAndroidInjector
    abstract fun contributeResetPasswordFragment(): ResetPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeSquareFragment(): SquareFragment

    @ContributesAndroidInjector
    abstract fun contributeMessageFragment(): MessageFragment

    @ContributesAndroidInjector
    abstract fun contributeMineFragment(): MineFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateDatingFragment(): CreateDatingFragment

    @ContributesAndroidInjector
    abstract fun contributeDatingListFragment(): DatingListFragment

    @ContributesAndroidInjector
    abstract fun contributeWalletFragment(): WalletFragment

    @ContributesAndroidInjector
    abstract fun contributeNearbyFragment(): NearbyFragment

}