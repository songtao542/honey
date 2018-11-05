package com.snt.phoney.di.module

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
import com.snt.phoney.ui.settings.SettingsFragment
import com.snt.phoney.ui.signin.SigninFragment
import com.snt.phoney.ui.signin.StartupFragment
import com.snt.phoney.ui.signup.*
import com.snt.phoney.ui.square.SquareFragment
import com.snt.phoney.ui.square.official.OfficialRecommendFragment
import com.snt.phoney.ui.square.popular.PopularRecommendFragment
import com.snt.phoney.ui.user.EditUserFragment
import com.snt.phoney.ui.user.FollowmeFragment
import com.snt.phoney.ui.user.UserInfoFragment
import com.snt.phoney.ui.user.VisitorFragment
import com.snt.phoney.ui.vip.VipFragment
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
    abstract fun contributeStartupFragment(): StartupFragment

    @ContributesAndroidInjector
    abstract fun contributeBindPhoneFragment(): BindPhoneFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

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
    abstract fun contributeStepOneFragment(): StepOneFragment

    @ContributesAndroidInjector
    abstract fun contributeStepTwoFragment(): StepTwoFragment

    @ContributesAndroidInjector
    abstract fun contributeStepThreeFragment(): StepThreeFragment

    @ContributesAndroidInjector
    abstract fun contributeEditUserFragment(): EditUserFragment

    @ContributesAndroidInjector
    abstract fun contributeVisitorFragment(): VisitorFragment

    @ContributesAndroidInjector
    abstract fun contributeFollowmeFragment(): FollowmeFragment

    @ContributesAndroidInjector
    abstract fun contributeUserInfoFragment(): UserInfoFragment

}