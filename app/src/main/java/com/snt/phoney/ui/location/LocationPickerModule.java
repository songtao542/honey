package com.snt.phoney.ui.location;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;

import com.snt.phoney.base.AppViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class LocationPickerModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract LocationPickerFragment contributeLocationPickerFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract LocationPicker contributeLocationPicker();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(LocationViewModel.class)
    public abstract AppViewModel bindLocationViewModel(LocationViewModel viewModel);

}