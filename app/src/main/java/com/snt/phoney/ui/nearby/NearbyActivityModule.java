package com.snt.phoney.ui.nearby;

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
public abstract class NearbyActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract NearbyFragment contributeNearbyFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(NearbyViewModel.class)
    public abstract ViewModel bindNearbyViewModel(NearbyViewModel viewModel);
}
