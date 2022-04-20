package com.skedgo.tripkit

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import com.skedgo.tripkit.configuration.GetAppVersion
import com.skedgo.tripkit.configuration.Key
import java.io.IOException
import java.util.*
import java.util.concurrent.Callable

class AddCustomHeaders constructor(
    private val getAppVersion: GetAppVersion,
    private val getLocale: () -> Locale,
    private val getUuid: Callable<String>?,
    private val getUserToken: Callable<String>?,
    private val getKey: () -> Key,
    private val preferences: SharedPreferences?
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
            .addHeader(appVersionHeader, getAppVersion.execute().blockingFirst())
        builder.addHeader(acceptHeader, appJsonValue)

        val key = getKey()
        when (key) {
            is Key.ApiKey -> builder.addHeader(keyHeader, key.value)
            is Key.RegionEligibility -> builder.addHeader(regionEligibilityHeader, key.value)
        }

        if (getUserToken != null && getUserToken.call() != null) {
            builder.addHeader(userTokenHeader, getUserToken.call())
        }

        val hasTripSelection = preferences?.getBoolean("tripSelection", false) ?: false
        if (getUuid != null && hasTripSelection) {
            val uuid = getUuid.call()
            if (uuid != null) {
                builder.addHeader(uuidHeader, uuid)
            }
        }
        return chain.proceed(builder.build())
    }
}
