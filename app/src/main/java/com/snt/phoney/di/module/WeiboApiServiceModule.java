package com.snt.phoney.di.module;

import com.snt.phoney.BuildConfig;
import com.snt.phoney.api.WeiboApi;
import com.snt.phoney.utils.adapter.LiveDataCallAdapterFactory;
import com.snt.phoney.utils.data.Constants;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class WeiboApiServiceModule {

    @Singleton
    @Provides
    @Named("weibo_api")
    public static Retrofit provideApiRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.Weibo.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(getOkHttpClientBuilder().build())
                .build();
    }

    private static OkHttpClient.Builder getOkHttpClientBuilder() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpBuilder.addInterceptor(httpLoggingInterceptor);
        }

        okHttpBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            HttpUrl url = request.url().newBuilder()
                    //.addQueryParameter(Constants.Api.APP_ID, Constants.Api.APP_ID_VALUE)
                    .build();
            return chain.proceed(request.newBuilder().url(url).build());
        });
        return okHttpBuilder;
    }

    @Singleton
    @Provides
    public static WeiboApi provideWeiboApi(@Named("weibo_api") Retrofit retrofit) {
        return retrofit.create(WeiboApi.class);
    }
}