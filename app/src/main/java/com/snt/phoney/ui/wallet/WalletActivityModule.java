package com.snt.phoney.ui.wallet;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class WalletActivityModule {


    @ContributesAndroidInjector
    public abstract WalletFragment contributeWalletFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WalletViewModel.class)
    public abstract ViewModel bindWalletViewModel(WalletViewModel viewModel);
}
