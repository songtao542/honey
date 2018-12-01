package com.snt.phoney.ui.picker;

import com.snt.phoney.di.FragmentScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PickerModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract PickerFragment contributePickerFragment();

}
