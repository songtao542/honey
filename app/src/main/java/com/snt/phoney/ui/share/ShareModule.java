package com.snt.phoney.ui.share;

import com.snt.phoney.di.FragmentScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ShareModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract ShareFragment contributeShareFragment();

}
