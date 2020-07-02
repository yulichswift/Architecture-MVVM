package com.jeff.architecture_mvvm.model.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.HttpURLConnection

class AuthInterceptor : Interceptor, KoinComponent {

    private val apiRepository: ApiRepository by inject()

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.buildRequest())

        return when (response.code) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                //TODO: do refresh token, then new token save to pref.token
                /*
                CoroutineScope(Dispatchers.IO).launch {
                    apiRepository.refreshToken()
                }
                */
                chain.proceed(chain.buildRequest())
            }
            else -> response
        }
    }

    private fun Interceptor.Chain.buildRequest(): Request {
        val requestBuilder = request().newBuilder()
        return requestBuilder.build()
    }
}