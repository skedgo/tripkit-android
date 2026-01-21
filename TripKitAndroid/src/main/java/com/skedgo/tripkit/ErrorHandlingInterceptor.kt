package com.skedgo.tripkit

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import java.net.UnknownHostException

open class ErrorHandlingInterceptor(
    private val appDeactivatedListener: (() -> Unit)? = null
) : Interceptor {

    private val tag = ErrorHandlingInterceptor::class.java.name

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url
        return try {
            Timber.tag(tag).i("Executing API request: %s", url)

            val isRegionsEndpoint = url.encodedPath.contains("regions.json")
            val response = chain.proceed(request)

            if (!response.isSuccessful && isRegionsEndpoint) {
                if (response.code == 401) {
                    appDeactivatedListener?.invoke()
                }
            }

            // Handle 502 Bad Gateway with structured logging and propagation
            if (response.code == 502) {
                Timber.tag(tag).w("HTTP 502 Bad Gateway: %s", url)

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

            response
        } catch (e: UnknownHostException) {
            // DNS/network issues are expected in the wild (e.g. captive portals, bad connectivity).
            // Log for diagnostics, but do NOT replace the exception (we want to preserve the cause/stack).
            val formattedMessage = "API Request Failed: $url - ${e.message}"
            Timber.tag(tag).w(e, formattedMessage)
            throw e
        } catch (e: IOException) {
            // Handle network or other exceptions
            // Log the exception before rethrowing for proper debugging
            Timber.tag(tag).e(e, "Network error for URL: %s - %s", url, e.message)
            throw e
        }
    }
}