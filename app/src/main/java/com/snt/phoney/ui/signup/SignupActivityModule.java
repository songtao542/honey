package com.snt.phoney.ui.signup;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class SignupActivityModule {
    @FragmentScope
    @ContributesAndroidInjector
    public abstract StartupFragment contributeStartupFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract SignupFragment contributeSignupFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(StartupViewModel.class)
    public abstract ViewModel bindStartupViewModel(StartupViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WxViewModel.class)
    public abstract ViewModel bindWxViewModel(WxViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(WeiboViewModel.class)
    public abstract ViewModel bindWeiboViewModel(WeiboViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(QQViewModel.class)
    public abstract ViewModel bindQQViewModel(QQViewModel viewModel);

}
