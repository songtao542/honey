package com.snt.phoney.di.component;

import android.app.Application;

import com.snt.phoney.base.App;
import com.snt.phoney.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                AppModule.class}
)
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        AppComponent build();

        @BindsInstance
        Builder application(Application application);
    }

    void inject(App app);
}
