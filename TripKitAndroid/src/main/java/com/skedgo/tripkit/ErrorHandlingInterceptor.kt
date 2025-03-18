package com.skedgo.tripkit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.UnknownHostException
import java.util.logging.Level
import java.util.logging.Logger

open class ErrorHandlingInterceptor(
    private val appDeactivatedListener: (() -> Unit)? = null
) : Interceptor {

    private val tag = ErrorHandlingInterceptor::class.java.name // Use class name as log tag

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        try {
            Log.i(tag, "Executing API request: $url")

            val isRegionsEndpoint = url.encodedPath.contains("regions.json")
            val response = chain.proceed(request)

            if (!response.isSuccessful && isRegionsEndpoint) {
                if (response.code == 401) {
                    appDeactivatedListener?.invoke()
                }
            }

            return response
        } catch (e: UnknownHostException) {
            handleUnknownHostException(e, url.toString())
        } catch (e: IOException) {
            // Handle network or other exceptions
            // You can log or handle these exceptions as needed
            e.printStackTrace()
            throw e
        }
    }

    private fun handleUnknownHostException(e: UnknownHostException, url: String): Nothing {
        val formattedMessage = "API Request Failed: $url - ${e.message}"

        Log.e(tag, formattedMessage, e)

        System.err.println(formattedMessage)
        e.printStackTrace()

        throw UnknownHostException(formattedMessage).apply { stackTrace = e.stackTrace }
    }
}