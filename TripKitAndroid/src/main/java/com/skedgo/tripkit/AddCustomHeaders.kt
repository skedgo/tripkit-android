package com.skedgo.tripkit

import android.content.SharedPreferences
import com.google.gson.Gson
import com.skedgo.tripkit.configuration.GetAppVersion
import com.skedgo.tripkit.configuration.Key
import com.skedgo.tripkit.data.HttpClientCustomDataStore
import com.skedgo.tripkit.extensions.fromJson
import okhttp3.Interceptor
import okhttp3.Response
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
    private val appJsonValue = "*/*"
    private val acceptHeader = "Accept"
    private val userTokenHeader = "userToken"
    private val deviceHeader = "X-TripGo-Device-Id"
    private val clientIdHeader = "X-TripGo-Client-Id"

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

        getUserToken?.call()?.let { token ->
            if (token.isNotEmpty()) {
                builder.addHeader(userTokenHeader, token)
            }
        }

        val hasTripSelection = preferences?.getBoolean("tripSelection", false) ?: false
        if (getUuid != null) {
            builder.addHeader(deviceHeader, getUuid.call())

            if (hasTripSelection) {
                val uuid = getUuid.call()
                if (uuid != null) {
                    builder.addHeader(uuidHeader, uuid)
                }
            }
        }

        if (HttpClientCustomDataStore.getCustomHeadersString().isNullOrBlank().not()) {
            val headersMap: Map<String, String> = Gson().fromJson(
                HttpClientCustomDataStore.getCustomHeadersString()
                    ?: ""
            )
            headersMap.forEach {
                builder.addHeader(it.key, it.value)
            }
        }

        preferences?.getString(TripKitConstants.PREF_KEY_CLIENT_ID, null)?.let { clientId ->
            builder.addHeader(clientIdHeader, clientId)
        }

        return chain.proceed(builder.build())
    }
}


