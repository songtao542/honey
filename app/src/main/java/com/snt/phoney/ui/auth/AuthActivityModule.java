package com.snt.phoney.ui.auth;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelFactoryModule.class})
public abstract class AuthActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract VideoAuthFragment contributeVideoAuthFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract AuthFragment contributeAuthFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract AuthModeFragment contributeAuthModeFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindDatingAuthViewModel(AuthViewModel viewModel);

}
