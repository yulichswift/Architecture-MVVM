package com.jeff.architecture_mvvm.di

import com.jeff.architecture_mvvm.BuildConfig
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.ApiService
import com.jeff.architecture_mvvm.model.api.ReplaceBaseUrlCallFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DiHttpLoggingInterceptor

    @Singleton
    @DiHttpLoggingInterceptor
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = when (BuildConfig.DEBUG) {
            true -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(@DiHttpLoggingInterceptor httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_HOST)
            .callFactory(object : ReplaceBaseUrlCallFactory(okHttpClient, BuildConfig.API_HOST) {
                override fun getReplaceUrl(): String {
                    // throw Exception("No host")
                    return "https://jeff.com"
                }
            })
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideApiRepository(apiService: ApiService): ApiRepository {
        return ApiRepository(apiService)
    }
}
