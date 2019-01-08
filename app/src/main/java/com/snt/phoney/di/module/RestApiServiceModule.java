package com.snt.phoney.di.module;

import android.app.Application;

import com.google.gson.Gson;
import com.snt.phoney.BuildConfig;
import com.snt.phoney.api.Api;
import com.snt.phoney.api.LoginStateInterceptor;
import com.snt.phoney.api.NullOrEmptyInterceptor;
import com.snt.phoney.api.TimeoutInterceptor;
import com.snt.phoney.utils.adapter.GsonResponseConverterFactory;
import com.snt.phoney.utils.data.Constants;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

@Module
public class RestApiServiceModule {


    @Singleton
    @Provides
    public static Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    @Named("api")
    public static Retrofit provideApiRetrofit(Application application, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Constants.Api.BASE_URL)
                .addConverterFactory(GsonResponseConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(getOkHttpClientBuilder(application, gson).build())
                .build();
    }

    private static OkHttpClient.Builder getOkHttpClientBuilder(Application application, Gson gson) {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder
                .addInterceptor(new NullOrEmptyInterceptor())
                .addInterceptor(new TimeoutInterceptor())
                .addInterceptor(new LoginStateInterceptor(application, gson));

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(httpLoggingInterceptor);
        }

        return okHttpBuilder;
    }

    @Singleton
    @Provides
    public static Api provideApi(@Named("api") Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}


