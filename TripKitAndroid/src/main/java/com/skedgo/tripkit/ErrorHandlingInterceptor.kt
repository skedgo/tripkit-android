package com.skedgo.tripkit

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

open class ErrorHandlingInterceptor(
    private val appDeactivatedListener: (() -> Unit)? = null
) : Interceptor {

    private val tag = ErrorHandlingInterceptor::class.java.name // Use class name as log tag

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        try {
            Timber.i(tag, "Executing API request: $url")

            val isRegionsEndpoint = url.encodedPath.contains("regions.json")
            val response = chain.proceed(request)

            if (!response.isSuccessful && isRegionsEndpoint) {
                if (response.code == 401) {
                    appDeactivatedListener?.invoke()
                }
            }

            // Handle 502 Bad Gateway with structured logging and propagation
            if (response.code == 502) {
                Timber.w(tag, "HTTP 502 Bad Gateway: $url")

                // TODO: Add a retry mechanism if needed
                /*
                 * If the 502 error is temporary, consider retrying the request a few times
                 * before failing. Here’s a simple retry logic:
                 *
                 * val maxRetries = 3
                 * val retryDelayMillis = 3000L // Wait 3 seconds before retrying
                 *
                 * repeat(maxRetries) { attempt ->
                 *     Thread.sleep(retryDelayMillis) // Delay before retrying
                 *     val retryResponse = chain.proceed(request)
                 *     if (retryResponse.isSuccessful) return retryResponse
                 * }
                 *
                 * throw IOException("Server is temporarily unavailable after retries (HTTP 502)")
                 */

                throw IOException("Server temporarily unavailable (HTTP 502) at $url")
            }

            return response
        } catch (e: UnknownHostException) {
            handleUnknownHostException(e, url.toString())
        } catch (e: IOException) {
            // Handle network or other exceptions
            // Log the exception before rethrowing for proper debugging
            Timber.e(tag, "Network error for URL: $url - ${e.message}", e)
            throw e
        }
    }

    private fun handleUnknownHostException(e: UnknownHostException, url: String): Nothing {
        val formattedMessage = "API Request Failed: $url - ${e.message}"

        Timber.e(tag, formattedMessage, e)

        System.err.println(formattedMessage)
        e.printStackTrace()

        throw UnknownHostException(formattedMessage).apply { stackTrace = e.stackTrace }
    }
}