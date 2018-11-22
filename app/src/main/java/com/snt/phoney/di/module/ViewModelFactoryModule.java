package com.snt.phoney.di.module;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.ViewModelFactory;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    @ActivityScope
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
