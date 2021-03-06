package com.snt.phoney.ui.album;

import androidx.lifecycle.ViewModel;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.di.FragmentScope;
import com.snt.phoney.di.ViewModelKey;
import com.snt.phoney.di.module.ViewModelFactoryModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module(includes = {ViewModelFactoryModule.class})
public abstract class AlbumActivityModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract PaySettingFragment contributePaySettingFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract AlbumViewerFragment contributeAlbumViewerFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract AlbumPhotoFragment contributeAlbumPhotoFragment();

    @FragmentScope
    @ContributesAndroidInjector
    public abstract PhotoApplyListFragment contributePhotoApplyListFragment();

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(PaySettingViewModel.class)
    public abstract ViewModel bindPaySettingViewModel(PaySettingViewModel viewModel);

    @Binds
    @IntoMap
    @ActivityScope
    @ViewModelKey(AlbumViewModel.class)
    public abstract ViewModel bindAlbumViewerViewModel(AlbumViewModel viewModel);


}
