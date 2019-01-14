package com.snt.phoney.di.module;

import com.snt.phoney.service.TaskIntentService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ServiceContributorModule {

    //@ContributesAndroidInjector
    //public abstract VoiceCallService contributeVoiceCallService();

    @ContributesAndroidInjector
    public abstract TaskIntentService contributeTaskIntentService();

}