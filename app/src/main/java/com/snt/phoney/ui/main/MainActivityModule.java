package com.snt.phoney.ui.main;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.mine.MineViewModel;
import com.snt.phoney.ui.privacy.CreateLockViewModel;
import com.snt.phoney.ui.share.ShareFragment;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class MainActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract ShareFragment contributeShareFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(MineViewModel.class)
    public abstract ViewModel bindMineViewModel(MineViewModel viewModel);
}
