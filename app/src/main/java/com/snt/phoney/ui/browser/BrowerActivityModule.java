package com.snt.phoney.ui.browser;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;
import com.snt.phoney.ui.dating.create.CreateDatingFragment;
import com.snt.phoney.ui.dating.create.CreateDatingViewModel;
import com.snt.phoney.ui.dating.detail.DatingDetailFragment;
import com.snt.phoney.ui.dating.detail.DatingDetailViewModel;
import com.snt.phoney.ui.dating.list.DatingListFragment;
import com.snt.phoney.ui.dating.list.DatingViewModel;
import com.snt.phoney.ui.location.LocationPickerModule;
import com.snt.phoney.ui.main.message.MessageViewModel;
import com.snt.phoney.ui.photo.PhotoViewerModule;
import com.snt.phoney.ui.picker.PickerModule;

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
