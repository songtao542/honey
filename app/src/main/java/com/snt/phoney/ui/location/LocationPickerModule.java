package com.snt.phoney.ui.location;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;

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
    public abstract ViewModel bindLocationViewModel(LocationViewModel viewModel);

}