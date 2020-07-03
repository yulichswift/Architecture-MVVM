package com.jeff.architecture_mvvm.di

import com.jeff.architecture_mvvm.BuildConfig
import com.jeff.architecture_mvvm.model.api.ApiRepository
import com.jeff.architecture_mvvm.model.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {
    single { provideHttpLoggingInterceptor() }
    single { provideOkHttpClient(get()) }
    single { provideApiService(get()) }
    single { provideApiRepository(get()) }
}

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = when (BuildConfig.DEBUG) {
        true -> HttpLoggingInterceptor.Level.BODY
        else -> HttpLoggingInterceptor.Level.NONE
    }
    return httpLoggingInterceptor
}

private fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

private fun provideApiService(okHttpClient: OkHttpClient): ApiService {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(BuildConfig.API_HOST)
        .build()
        .create(ApiService::class.java)
}

private fun provideApiRepository(apiService: ApiService): ApiRepository {
    return ApiRepository(apiService)
}
