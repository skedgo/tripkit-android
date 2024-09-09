package com.skedgo.tripkit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorHandlingInterceptor(
    private val appDeactivatedListener: (() -> Unit)? = null
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        try {
            val isRegionsEndpoint = url.encodedPath.contains("regions.json")
            val response = chain.proceed(request)

            if (!response.isSuccessful && isRegionsEndpoint) {
                if(response.code == 401) {
                    appDeactivatedListener?.invoke()
                }
            }

            return response
        } catch (e: IOException) {
            // Handle network or other exceptions
            // You can log or handle these exceptions as needed
            e.printStackTrace()
            throw e
        }
    }
}