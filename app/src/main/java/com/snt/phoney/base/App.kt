/*
 *
 * Copyright (C) 2018. Faraday&Future
 * All rights reserved.
 * PROPRIETARY AND CONFIDENTIAL.
 * NOTICE: All information contained herein is, and remains the property of Faraday&Future Inc.
 * The intellectual and technical concepts contained herein are proprietary to Faraday&Future Inc.
 * and may be covered by U.S. and Foreign Patents, patents in process, and are protected
 * by trade secret and copyright law. Dissemination of this code or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained from Faraday&Future Inc.
 * Access to the source code contained herein is hereby forbidden to anyone except current
 * Faraday&Future Inc. employees or others who have executed Confidentiality and
 *  Non-disclosure agreements covering such access.
 *
 *
 */

package com.snt.phoney.base

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.snt.phoney.BuildConfig
import com.snt.phoney.di.AppInjector
import com.snt.phoney.di.component.AppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject
import android.support.multidex.MultiDex


class App : MultiDexApplication(), HasActivityInjector {

    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}