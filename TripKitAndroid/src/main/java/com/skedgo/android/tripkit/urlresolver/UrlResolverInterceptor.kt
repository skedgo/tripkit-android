package com.skedgo.android.tripkit.urlresolver

import android.text.TextUtils
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class UrlResolverInterceptor(private val getHitServers: GetHitServers,
                             private val getBaseServer: GetBaseServer) : Interceptor {


  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {


    val request = chain.request()
    val requestUrl = request.url()

    val baseUrl: String = getBaseServer.execute().toBlocking().first()


    if (requestUrl.toString().startsWith(baseUrl)) {

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

        if (response.isSuccessful) {
          return response
        }
      }
    }

    return chain.proceed(request)

  }


}