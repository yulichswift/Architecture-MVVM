package com.jeff.architecture_mvvm.model.api

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

abstract class ReplaceBaseUrlCallFactory(private val client: OkHttpClient, private val baseUrl: String) : Call.Factory {

    abstract fun getReplaceUrl(): String

    override fun newCall(request: Request): Call {
        val url = request.url.toString()
        val newRequest = when {
            url.startsWith(baseUrl) -> {
                val newUrl = url.replace(baseUrl, getReplaceUrl())
                request.newBuilder().url(newUrl).build()
            }
            else -> request
        }

        return client.newCall(newRequest)
    }
}
