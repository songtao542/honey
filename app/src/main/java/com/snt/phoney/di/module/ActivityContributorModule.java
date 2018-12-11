package com.snt.phoney.di.module;

import com.snt.phoney.di.ActivityScope;
import com.snt.phoney.ui.dating.DatingActivity;
import com.snt.phoney.ui.dating.DatingActivityModule;
import com.snt.phoney.ui.main.MainActivity;
import com.snt.phoney.ui.main.MainActivityModule;
import com.snt.phoney.ui.nearby.NearbyActivity;
import com.snt.phoney.ui.nearby.NearbyActivityModule;
import com.snt.phoney.ui.photo.PhotoViewerActivity;
import com.snt.phoney.ui.photo.PhotoViewerModule;
import com.snt.phoney.ui.privacy.PrivacyActivity;
import com.snt.phoney.ui.privacy.PrivacyActivityModule;
import com.snt.phoney.ui.report.ReportActivity;
import com.snt.phoney.ui.report.ReportActivityModule;
import com.snt.phoney.ui.setup.SetupWizardActivity;
import com.snt.phoney.ui.setup.SetupWizardActivityModule;
import com.snt.phoney.ui.signup.SignupActivity;
import com.snt.phoney.ui.signup.SignupActivityModule;
import com.snt.phoney.ui.user.UserActivity;
import com.snt.phoney.ui.user.UserActivityModule;
import com.snt.phoney.ui.vip.VipActivity;
import com.snt.phoney.ui.vip.VipActivityModule;
import com.snt.phoney.wxapi.WXEntryActivity;
import com.snt.phoney.wxapi.WXEntryActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityContributorModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            SignupActivityModule.class
    })
    public abstract SignupActivity contributeSignupActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            WXEntryActivityModule.class
    })
    public abstract WXEntryActivity contributeWXEntryActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            SetupWizardActivityModule.class
    })
    public abstract SetupWizardActivity contributeSetupWizardActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            MainActivityModule.class
    })
    public abstract MainActivity contributeMainActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            NearbyActivityModule.class
    })
    public abstract NearbyActivity contributeNearbyActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            VipActivityModule.class
    })
    public abstract VipActivity contributeVipActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            UserActivityModule.class
    })
    public abstract UserActivity contributeUserInfoActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            DatingActivityModule.class
    })
    public abstract DatingActivity contributeDatingActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            ReportActivityModule.class
    })
    public abstract ReportActivity contributeReportActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            PrivacyActivityModule.class
    })
    public abstract PrivacyActivity contributePrivacyActivity();

    @ActivityScope
    @ContributesAndroidInjector(modules = {
            EmptyViewModelFactoryModule.class,
            PhotoViewerModule.class
    })
    public abstract PhotoViewerActivity contributePhotoViewerActivity();
}