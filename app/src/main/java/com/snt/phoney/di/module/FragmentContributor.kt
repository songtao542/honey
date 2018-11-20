package com.snt.phoney.di.module

import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.di.ViewModelFactory
import com.snt.phoney.ui.dating.create.CreateDatingFragment
import com.snt.phoney.ui.dating.detail.DatingDetailFragment
import com.snt.phoney.ui.dating.list.DatingListFragment
import com.snt.phoney.ui.home.HomeFragment
import com.snt.phoney.ui.home.following.FollowingFragment
import com.snt.phoney.ui.home.friend.FriendFragment
import com.snt.phoney.ui.message.MessageFragment
import com.snt.phoney.ui.mine.MineFragment
import com.snt.phoney.ui.nearby.NearbyFragment
import com.snt.phoney.ui.password.ForgetPasswordFragment
import com.snt.phoney.ui.privacy.AlbumPermissionSettingFragment
import com.snt.phoney.ui.privacy.CreateLockFragment
import com.snt.phoney.ui.privacy.CreateLockStep1Fragment
import com.snt.phoney.ui.privacy.CreateLockStep2Fragment
import com.snt.phoney.ui.report.ReportFragment
import com.snt.phoney.ui.settings.SettingsFragment
import com.snt.phoney.ui.setup.*
import com.snt.phoney.ui.share.ShareFragment
import com.snt.phoney.ui.signup.SignupFragment
import com.snt.phoney.ui.signup.StartupFragment
import com.snt.phoney.ui.square.SquareFragment
import com.snt.phoney.ui.square.official.OfficialRecommendFragment
import com.snt.phoney.ui.square.popular.PopularRecommendFragment
import com.snt.phoney.ui.user.EditUserFragment
import com.snt.phoney.ui.user.FollowmeFragment
import com.snt.phoney.ui.user.UserInfoFragment
import com.snt.phoney.ui.user.VisitorFragment
import com.snt.phoney.ui.vip.VipFragment
import com.snt.phoney.ui.wallet.WalletFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

//@Module
//abstract class ViewModelFactoryModule {
//    @Binds
//    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
//}

@Module
abstract class WXEntryActivityModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

@Module
abstract class SignupActivityModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    abstract fun contributeStartupFragment(): StartupFragment

    @ContributesAndroidInjector
    abstract fun contributeSignupFragment(): SignupFragment
}

@Module
abstract class SetupWizardActivityModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    abstract fun contributeSetupWizardFragment(): SetupWizardFragment

    @ContributesAndroidInjector
    abstract fun contributeSetupWizardOneFragment(): SetupWizardOneFragment


    @ContributesAndroidInjector
    abstract fun contributeSetupWizardTwoFragment(): SetupWizardTwoFragment

    @ContributesAndroidInjector
    abstract fun contributeSetupWizardThreeFragment(): SetupWizardThreeFragment
}

@Module
abstract class MainActivityModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment


    @ContributesAndroidInjector
    abstract fun contributeFriendFragment(): FriendFragment


    @ContributesAndroidInjector
    abstract fun contributeFollowingFragment(): FollowingFragment


    @ContributesAndroidInjector
    abstract fun contributeSquareFragment(): SquareFragment


    @ContributesAndroidInjector
    abstract fun contributeOfficialRecommendFragment(): OfficialRecommendFragment


    @ContributesAndroidInjector
    abstract fun contributePopularRecommendFragment(): PopularRecommendFragment


    @ContributesAndroidInjector
    abstract fun contributeMessageFragment(): MessageFragment


    @ContributesAndroidInjector
    abstract fun contributeMineFragment(): MineFragment
}

@Module
abstract class CommonActivityModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector
    abstract fun contributeBindPhoneFragment(): BindPhoneFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateDatingFragment(): CreateDatingFragment

    @ContributesAndroidInjector
    abstract fun contributeDatingListFragment(): DatingListFragment

    @ContributesAndroidInjector
    abstract fun contributeDatingDetailFragment(): DatingDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeWalletFragment(): WalletFragment

    @ContributesAndroidInjector
    abstract fun contributeNearbyFragment(): NearbyFragment

    @ContributesAndroidInjector
    abstract fun contributeVipFragment(): VipFragment

    @ContributesAndroidInjector
    abstract fun contributeForgetPasswordFragment(): ForgetPasswordFragment

    @ContributesAndroidInjector
    abstract fun contributePickerFragment(): PickerFragment

    @ContributesAndroidInjector
    abstract fun contributeEditUserFragment(): EditUserFragment

    @ContributesAndroidInjector
    abstract fun contributeVisitorFragment(): VisitorFragment

    @ContributesAndroidInjector
    abstract fun contributeFollowmeFragment(): FollowmeFragment

    @ContributesAndroidInjector
    abstract fun contributeUserInfoFragment(): UserInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeReportFragment(): ReportFragment

    @ContributesAndroidInjector
    abstract fun contributeShareFragment(): ShareFragment

    @ContributesAndroidInjector
    abstract fun contributeAlbumPermissionSettingFragment(): AlbumPermissionSettingFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateLockFragment(): CreateLockFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateLockStep1Fragment(): CreateLockStep1Fragment

    @ContributesAndroidInjector
    abstract fun contributeCreateLockStep2Fragment(): CreateLockStep2Fragment
}