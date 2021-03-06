package com.snt.phoney.ui.dating;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.dating.apply.ApplicantListFragment;
import com.snt.phoney.ui.dating.apply.DatingApplyViewModel;
import com.snt.phoney.ui.dating.create.CreateDatingFragment;
import com.snt.phoney.ui.dating.create.CreateDatingViewModel;
import com.snt.phoney.ui.dating.detail.DatingDetailFragment;
import com.snt.phoney.ui.dating.detail.DatingDetailViewModel;
import com.snt.phoney.ui.dating.list.DatingListFragment;
import com.snt.phoney.ui.dating.list.DatingViewModel;
import com.snt.phoney.ui.dating.list.OthersDatingFragment;
import com.snt.phoney.ui.location.LocationPickerModule;
import com.snt.phoney.ui.photo.PhotoViewerModule;
import com.snt.phoney.ui.picker.PickerModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelFactoryModule.class, LocationPickerModule.class, PickerModule.class, PhotoViewerModule.class})
public abstract class DatingActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract CreateDatingFragment contributeCreateDatingFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract DatingListFragment contributeDatingListFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract DatingDetailFragment contributeDatingDetailFragment();


    @FragmentScope
    @ContributesAndroidInjector
    public abstract ApplicantListFragment contributeApplicantListFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract OthersDatingFragment contributeOthersDatingFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(CreateDatingViewModel.class)
    public abstract ViewModel bindCreateDatingViewModel(CreateDatingViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(DatingViewModel.class)
    public abstract ViewModel bindDatingViewModel(DatingViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(DatingApplyViewModel.class)
    public abstract ViewModel bindDatingApplyViewModel(DatingApplyViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(DatingDetailViewModel.class)
    public abstract ViewModel bindDatingDetailViewModel(DatingDetailViewModel viewModel);

}
