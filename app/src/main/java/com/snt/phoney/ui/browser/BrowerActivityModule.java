package com.snt.phoney.ui.browser;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.main.message.MessageViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelFactoryModule.class})
public abstract class BrowerActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract OfficialMessageFragment contributeOfficialMessageFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(MessageViewModel.class)
    public abstract ViewModel bindMessageViewModel(MessageViewModel viewModel);

}
