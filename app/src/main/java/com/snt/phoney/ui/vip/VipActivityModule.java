package com.snt.phoney.ui.vip;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class VipActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract VipFragment contributeVipFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(VipViewModel.class)
    public abstract ViewModel bindVipViewModel(VipViewModel viewModel);
}
