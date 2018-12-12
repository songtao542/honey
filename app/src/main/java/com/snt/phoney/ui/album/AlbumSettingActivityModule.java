package com.snt.phoney.ui.album;

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
public abstract class AlbumSettingActivityModule {


    @FragmentScope
    @ContributesAndroidInjector
    public abstract PaySettingFragment contributePaySettingFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(PaySettingViewModel.class)
    public abstract ViewModel bindPaySettingViewModel(PaySettingViewModel viewModel);


}
