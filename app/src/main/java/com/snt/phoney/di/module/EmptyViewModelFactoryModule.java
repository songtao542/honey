package com.snt.phoney.di.module;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.snt.phoney.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class EmptyViewModelFactoryModule {

    @Provides
    @ActivityScope
    public static ViewModelProvider.Factory provideViewModelFactory() {
        return new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                throw new IllegalStateException("Empty ViewModel factory can not create ViewModel!");
            }
        };
    }
}
