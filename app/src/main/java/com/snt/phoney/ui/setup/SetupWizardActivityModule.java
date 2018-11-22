package com.snt.phoney.ui.setup;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelFactory;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = ViewModelFactoryModule.class)
public abstract class SetupWizardActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract PickerFragment contributePickerFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract BindPhoneFragment contributeBindPhoneFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract SetupWizardFragment contributeSetupWizardFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract SetupWizardOneFragment contributeSetupWizardOneFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract SetupWizardTwoFragment contributeSetupWizardTwoFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract SetupWizardThreeFragment contributeSetupWizardThreeFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(SetupWizardViewModel.class)
    public abstract ViewModel bindSetupWizardViewModel(SetupWizardViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(BindPhoneViewModel.class)
    public abstract ViewModel bindBindPhoneViewModel(BindPhoneViewModel viewModel);


}
