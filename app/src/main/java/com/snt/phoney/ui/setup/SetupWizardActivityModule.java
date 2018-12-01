package com.snt.phoney.ui.setup;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.picker.PickerModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelFactoryModule.class, PickerModule.class})
public abstract class SetupWizardActivityModule {

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
