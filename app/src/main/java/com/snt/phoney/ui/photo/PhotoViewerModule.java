package com.snt.phoney.ui.photo;

import com.snt.phoney.di.FragmentScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PhotoViewerModule {

    @FragmentScope
    @ContributesAndroidInjector
    public abstract PhotoViewerFragment contributePhotoViewerFragment();

}
