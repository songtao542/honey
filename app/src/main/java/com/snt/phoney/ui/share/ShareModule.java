package com.snt.phoney.ui.share;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ShareModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract ShareFragment contributeShareFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(ShareViewModel.class)
    public abstract ViewModel bindShareViewModel(ShareViewModel viewModel);

}
