package com.skedgo.tripkit

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.Callable

class BaseUrlOverridingInterceptor(
    private val baseUrlAdapter: Callable<String>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var newBaseUrl: String? = null
        try {
            newBaseUrl = baseUrlAdapter.call()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val request = chain.request()
        val requestUrl = request.url
        val pathSegments = requestUrl.pathSegments
        val isFromSkedGo = pathSegments[0] == "satapp" ||
            requestUrl.host.contains("skedgo.com") ||
            requestUrl.host.contains("buzzhives.com") ||
            requestUrl.host.contains("tripgo.com")

        return if (newBaseUrl != null && newBaseUrl.isNotEmpty() && isFromSkedGo && !requestUrl.host.contains("payments.tripgo.com")) {
            var tempUrl = requestUrl.newBuilder().removePathSegment(0).build()
            if (requestUrl.host == "galaxies.skedgo.com") {
                tempUrl = tempUrl.newBuilder().removePathSegment(0).removePathSegment(0).build()
            }
            val query = tempUrl.query
            val encodedPath = tempUrl.encodedPathSegments.joinToString("/")
            val newUrl = newBaseUrl.toHttpUrlOrNull()!!
                .newBuilder()
                .addEncodedPathSegments(encodedPath)
                .query(query)
                .build()
            val newRequest = request.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}