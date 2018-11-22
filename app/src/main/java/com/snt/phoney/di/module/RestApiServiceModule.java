package com.snt.phoney.di.module;

import com.snt.phoney.BuildConfig;
import com.snt.phoney.api.Api;
import com.snt.phoney.utils.adapter.LiveDataCallAdapterFactory;
import com.snt.phoney.utils.adapter.ResponseConverterFactory;
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

@Module
public class RestApiServiceModule {

    @Singleton
    @Provides
    @Named("api")
    public static Retrofit provideApiRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.Api.BASE_URL)
                .addConverterFactory(ResponseConverterFactory.create())
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
    public static Api provideApi(@Named("api") Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}