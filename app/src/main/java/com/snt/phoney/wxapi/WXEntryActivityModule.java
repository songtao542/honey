package com.snt.phoney.wxapi;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.ViewModelFactory;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.signup.WxViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class WXEntryActivityModule {

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WxViewModel.class)
    public abstract ViewModel bindWxViewModel(WxViewModel viewModel);

}
