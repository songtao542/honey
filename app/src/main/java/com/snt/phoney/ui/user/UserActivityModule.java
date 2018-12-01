package com.snt.phoney.ui.user;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.picker.PickerModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelFactoryModule.class, PickerModule.class})
public abstract class UserActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract FollowMeFragment contributeFollowMeFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract VisitorFragment contributeVisitorFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract EditUserFragment contributeEditUserFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract UserInfoFragment contributeUserInfoFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(UserInfoViewModel.class)
    public abstract ViewModel bindUserInfoViewModel(UserInfoViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(EditUserViewModel.class)
    public abstract ViewModel bindEditUserViewModel(EditUserViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(FollowMeViewModel.class)
    public abstract ViewModel bindFollowMeViewModel(FollowMeViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(VisitorViewModel.class)
    public abstract ViewModel bindVisitorViewModel(VisitorViewModel viewModel);

}
