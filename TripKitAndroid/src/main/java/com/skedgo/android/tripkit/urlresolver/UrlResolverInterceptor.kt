package com.skedgo.android.tripkit.urlresolver

import android.text.TextUtils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import skedgo.tripkit.configuration.Server
import skedgo.tripkit.urlresolver.GetHitServers
import java.io.IOException

class UrlResolverInterceptor(
    private val getHitServers: GetHitServers
) : Interceptor {
  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response? {
    val request = chain.request()
    val requestUrl = request.url()

    if (requestUrl.toString().startsWith(Server.Inflationary.value)) {
      val urls = getHitServers.execute().toBlocking().toIterable()
      urls.forEach url@ {
        val tempUrl = requestUrl.newBuilder().removePathSegment(0).build()
        val query = tempUrl.query()
        val encodedPath = TextUtils.join("/", tempUrl.encodedPathSegments())
        val newUrl = HttpUrl.parse(it)
            .newBuilder()
            .addEncodedPathSegments(encodedPath)
            .query(query)
            .build()
        val newRequest = request.newBuilder().url(newUrl).build()
        val response = chain.proceed(newRequest)
        if (response?.isSuccessful ?: false) {
          return response
        }
      }
    }
    return chain.proceed(request)
  }
}