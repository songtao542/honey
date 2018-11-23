package com.snt.phoney.ui.main;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.home.HomeFragment;
import com.snt.phoney.ui.home.following.FollowingFragment;
import com.snt.phoney.ui.home.friend.FriendFragment;
import com.snt.phoney.ui.home.friend.FriendViewModel;
import com.snt.phoney.ui.message.MessageFragment;
import com.snt.phoney.ui.mine.MineFragment;
import com.snt.phoney.ui.mine.MineViewModel;
import com.snt.phoney.ui.share.ShareFragment;
import com.snt.phoney.ui.square.SquareFragment;
import com.snt.phoney.ui.square.official.OfficialRecommendFragment;
import com.snt.phoney.ui.square.popular.PopularRecommendFragment;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class MainActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract HomeFragment contributeHomeFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract FriendFragment contributeFriendFragment();

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
    public abstract ShareFragment contributeShareFragment();

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
}
