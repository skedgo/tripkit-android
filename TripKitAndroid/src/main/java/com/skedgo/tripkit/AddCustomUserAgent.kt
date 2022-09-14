package com.skedgo.tripkit

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class AddCustomUserAgent constructor(
    private val appName: String,
    private val versionName: String
) : Interceptor {
  private val HEADER_USER_AGENT = "User-Agent"

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val newRequest = chain.request()
        .newBuilder()
        .header(HEADER_USER_AGENT, "$appName/$versionName")
        .build()
    return chain.proceed(newRequest)
  }
}
