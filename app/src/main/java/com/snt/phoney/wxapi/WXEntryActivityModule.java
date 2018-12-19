package com.snt.phoney.wxapi;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class WXEntryActivityModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WXAuthViewModel.class)
    public abstract ViewModel bindWXAuthViewModel(WXAuthViewModel viewModel);

}
