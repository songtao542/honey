package com.snt.phoney.ui.member;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class MemberActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract MemberFragment contributeMemberFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(MemberViewModel.class)
    public abstract ViewModel bindMemberViewModel(MemberViewModel viewModel);
}
