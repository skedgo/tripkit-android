package com.skedgo.android.tripkit

import okhttp3.Interceptor
import okhttp3.Response
import rx.functions.Func0
import skedgo.tripkit.configuration.GetAppVersion
import skedgo.tripkit.configuration.Key
import java.io.IOException
import java.util.*

internal class AddCustomHeaders constructor(
    private val getAppVersion: GetAppVersion,
    private val getLocale: () -> Locale,
    private val getUuid: Func0<String>?,
    private val getUserToken: Func0<String>?,
    private val getKey: () -> Key
) : Interceptor {
  private val appVersionHeader = "X-TripGo-Version"
  private val uuidHeader = "X-TripGo-UUID"
  private val keyHeader = "X-TripGo-Key"
  private val regionEligibilityHeader = "X-TripGo-RegionEligibility"
  private val acceptLanguageHeader = "Accept-Language"
  private val appJsonValue = "application/json"
  private val acceptHeader = "Accept"
  private val userTokenHeader = "userToken"

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val builder = chain.request().newBuilder()
        .addHeader(acceptLanguageHeader, getLocale().language)
        .addHeader(appVersionHeader, getAppVersion.execute().toBlocking().first())
        .addHeader(acceptHeader, appJsonValue)

    val key = getKey()
    when (key) {
      is Key.ApiKey -> builder.addHeader(keyHeader, key.value)
      is Key.RegionEligibility -> builder.addHeader(regionEligibilityHeader, key.value)
    }

    if (getUserToken != null && getUserToken.call() != null) {
      builder.addHeader(userTokenHeader, getUserToken.call())
    }

    if (getUuid != null) {
      val uuid = getUuid.call()
      if (uuid != null) {
        builder.addHeader(uuidHeader, uuid)
      }
    }
    return chain.proceed(builder.build())
  }
}
