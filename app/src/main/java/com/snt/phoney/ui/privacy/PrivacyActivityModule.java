package com.snt.phoney.ui.privacy;

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
public abstract class PrivacyActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract CreateLockStep1Fragment contributeCreateLockStep1Fragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract CreateLockStep2Fragment contributeCreateLockStep2Fragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract CreateLockFragment contributeCreateLockFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract LockFragment contributeLockFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract ForgetLockFragment contributeForgetLockFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(CreateLockViewModel.class)
    public abstract ViewModel bindCreateLockViewModel(CreateLockViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(ForgetPasswordViewModel.class)
    public abstract ViewModel bindForgetPasswordViewModel(ForgetPasswordViewModel viewModel);

}
