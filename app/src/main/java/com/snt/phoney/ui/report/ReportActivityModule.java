package com.snt.phoney.ui.report;

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
public abstract class ReportActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract ReportFragment contributeReportFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(ReportViewModel.class)
    public abstract ViewModel bindReportViewModel(ReportViewModel viewModel);

}
