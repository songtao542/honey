package com.snt.phoney.di.module

import com.snt.phoney.BuildConfig
import com.snt.phoney.api.WeiboApi
import com.snt.phoney.api.WxApi
import com.snt.phoney.utils.adapter.LiveDataCallAdapterFactory
import com.snt.phoney.utils.adapter.ResponseConverterFactory
import com.snt.phoney.utils.data.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object WxApiServiceModule {

    @JvmStatic
    @Singleton
    @Provides
    @Named("wx_api")
    fun provideWeiboApiRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.Wechat.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(getOkHttpClientBuilder().build())
                .build()
    }

    @JvmStatic
    private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
        val okHttpBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.addInterceptor(httpLoggingInterceptor)
        }

        okHttpBuilder.addInterceptor { chain ->
            val request = chain.request()
            val url = request.url().newBuilder()
                    .addQueryParameter("appid", Constants.Wechat.APP_ID)
                    .addQueryParameter("secret", Constants.Wechat.APP_SECRET)
                    .build()
            return@addInterceptor chain.proceed(request.newBuilder().url(url).build())
        }
        return okHttpBuilder
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideWeiboApi(@Named("wx_api") retrofit: Retrofit): WxApi = retrofit.create(WxApi::class.java)
}