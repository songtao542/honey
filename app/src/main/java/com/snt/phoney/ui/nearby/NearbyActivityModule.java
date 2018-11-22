package com.snt.phoney.ui.nearby;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.home.HomeFragment;
import com.snt.phoney.ui.home.following.FollowingFragment;
import com.snt.phoney.ui.home.friend.FriendFragment;
import com.snt.phoney.ui.message.MessageFragment;
import com.snt.phoney.ui.mine.MineFragment;
import com.snt.phoney.ui.square.SquareFragment;
import com.snt.phoney.ui.square.official.OfficialRecommendFragment;
import com.snt.phoney.ui.square.popular.PopularRecommendFragment;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class NearbyActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract NearbyFragment contributeNearbyFragment();

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

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(NearbyViewModel.class)
    public abstract ViewModel bindNearbyViewModel(NearbyViewModel viewModel);
}
