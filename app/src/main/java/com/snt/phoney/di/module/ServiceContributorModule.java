package com.snt.phoney.di.module;

import com.snt.phoney.service.VoiceCallService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ServiceContributorModule {

    @ContributesAndroidInjector
    public abstract VoiceCallService contributeVoiceCallService();

}