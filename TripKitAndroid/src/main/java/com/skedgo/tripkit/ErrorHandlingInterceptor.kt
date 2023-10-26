package com.skedgo.tripkit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorHandlingInterceptor(
    private val appDeactivatedListener: (() -> Unit)? = null
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)

            if (!response.isSuccessful) {
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