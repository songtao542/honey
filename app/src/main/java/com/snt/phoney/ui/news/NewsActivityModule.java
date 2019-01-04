package com.snt.phoney.ui.news;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.privacy.CreateLockFragment;
import com.snt.phoney.ui.privacy.CreateLockStep1Fragment;
import com.snt.phoney.ui.privacy.CreateLockStep2Fragment;
import com.snt.phoney.ui.privacy.CreateLockViewModel;
import com.snt.phoney.ui.privacy.LockFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class NewsActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract NewsFragment contributeNewsFragment();


    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(NewsViewModel.class)
    public abstract ViewModel bindNewsViewModel(NewsViewModel viewModel);

}
