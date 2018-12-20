package com.snt.phoney.ui.main;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.main.home.HomeFragment;
import com.snt.phoney.ui.main.home.following.FollowingFragment;
import com.snt.phoney.ui.main.home.following.FollowingViewModel;
import com.snt.phoney.ui.main.home.friend.FilterFragment;
import com.snt.phoney.ui.main.home.friend.FilterViewModel;
import com.snt.phoney.ui.main.home.friend.FriendFragment;
import com.snt.phoney.ui.main.home.friend.FriendViewModel;
import com.snt.phoney.ui.main.message.MessageFragment;
import com.snt.phoney.ui.main.mine.AlbumPermissionSettingFragment;
import com.snt.phoney.ui.main.mine.MineFragment;
import com.snt.phoney.ui.main.mine.MineViewModel;
import com.snt.phoney.ui.main.square.SquareFragment;
import com.snt.phoney.ui.main.square.SquareViewModel;
import com.snt.phoney.ui.main.square.official.OfficialRecommendFragment;
import com.snt.phoney.ui.main.square.popular.PopularRecommendFragment;
import com.snt.phoney.ui.setup.BindPhoneFragment;
import com.snt.phoney.ui.setup.BindPhoneViewModel;
import com.snt.phoney.ui.share.ShareFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelFactoryModule.class})
public abstract class MainActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract HomeFragment contributeHomeFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract FriendFragment contributeFriendFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract FilterFragment contributeFilterFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract FollowingFragment contributeFollowingFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract SquareFragment contributeSquareFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract OfficialRecommendFragment contributeOfficialRecommendFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract PopularRecommendFragment contributePopularRecommendFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract MessageFragment contributeMessageFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract MineFragment contributeMineFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract AlbumPermissionSettingFragment contributeAlbumPermissionSettingFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract ShareFragment contributeShareFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract BindPhoneFragment contributeBindPhoneFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(BindPhoneViewModel.class)
    public abstract ViewModel bindBindPhoneViewModel(BindPhoneViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(MineViewModel.class)
    public abstract ViewModel bindMineViewModel(MineViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(FriendViewModel.class)
    public abstract ViewModel bindFriendViewModel(FriendViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(FollowingViewModel.class)
    public abstract ViewModel bindFollowingViewModel(FollowingViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SquareViewModel.class)
    public abstract ViewModel bindSquareViewModel(SquareViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(FilterViewModel.class)
    public abstract ViewModel bindFilterViewModel(FilterViewModel viewModel);


}
