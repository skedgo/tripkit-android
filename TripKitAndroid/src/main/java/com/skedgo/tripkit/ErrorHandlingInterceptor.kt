package com.skedgo.tripkit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

open class ErrorHandlingInterceptor(
    private val appDeactivatedListener: (() -> Unit)? = null
) : Interceptor {

    private val logger = Logger.getLogger(ErrorHandlingInterceptor::class.java.name)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        try {
            val isRegionsEndpoint = url.encodedPath.contains("regions.json")
            val response = chain.proceed(request)

            if (!response.isSuccessful && isRegionsEndpoint) {
                if (response.code == 401) {
                    appDeactivatedListener?.invoke()
                }
            }

            // Handle 502 Bad Gateway with structured logging and propagation
            if (response.code == 502) {
                logger.log(Level.WARNING, "HTTP 502 Bad Gateway for URL: $url")

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

                throw IOException("Server is temporarily unavailable (HTTP 502)")
            }

            return response
        } catch (e: IOException) {
            // Handle network or other exceptions
            // Log the exception before rethrowing for proper debugging
            logger.log(Level.SEVERE, "Network error occurred: ${e.message}", e)
            throw e
        }
    }
}