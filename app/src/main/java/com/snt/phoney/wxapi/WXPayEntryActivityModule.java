package com.snt.phoney.wxapi;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class WXPayEntryActivityModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WXPayViewModel.class)
    public abstract ViewModel bindWXPayViewModel(WXPayViewModel viewModel);

}
