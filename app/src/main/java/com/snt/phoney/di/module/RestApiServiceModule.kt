package com.snt.phoney.di.module

import com.snt.phoney.BuildConfig
import com.snt.phoney.api.Api
import com.snt.phoney.utils.adapter.LiveDataCallAdapterFactory
import com.snt.phoney.utils.adapter.ResponseConverterFactory
import com.snt.phoney.utils.data.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object RestApiServiceModule {

    @JvmStatic
    @Singleton
    @Provides
    @Named("api")
    fun provideApiRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.Api.BASE_URL)
                .addConverterFactory(ResponseConverterFactory.create())
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

        okHttpBuilder.addInterceptor {
            val request = it.request()
            val url = request.url().newBuilder()
                    //.addQueryParameter(Constants.Api.APP_ID, Constants.Api.APP_ID_VALUE)
                    .build()
            return@addInterceptor it.proceed(request.newBuilder().url(url).build())
        }
        return okHttpBuilder
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideApi(@Named("api") retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}