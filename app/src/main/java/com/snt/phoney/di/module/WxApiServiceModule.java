package com.snt.phoney.di.module;

import com.google.gson.Gson;
import com.snt.phoney.BuildConfig;
import com.snt.phoney.api.WxApi;
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
public class WxApiServiceModule {

    @Singleton
    @Provides
    @Named("wx_api")
    public static Retrofit provideWeiboApiRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Constants.Wechat.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
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
                    .addQueryParameter("appid", Constants.Wechat.APP_ID)
                    .addQueryParameter("secret", Constants.Wechat.APP_SECRET)
                    .build();
            return chain.proceed(request.newBuilder().url(url).build());
        });
        return okHttpBuilder;
    }

    @Singleton
    @Provides
    public static WxApi provideWeiboApi(@Named("wx_api") Retrofit retrofit) {
        return retrofit.create(WxApi.class);
    }
}