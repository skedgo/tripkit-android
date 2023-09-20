package com.skedgo.tripkit.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

@SuppressLint("StaticFieldLeak")
object HttpClientCustomDataStore {

    private val gson = Gson()
    private var context: Context? = null
    private var sharedPreferences: SharedPreferences? = null
    private const val KEY_CUSTOM_HEADERS = "_custom_headers"
    private const val KEY_CUSTOM_BASE_URL = "_custom_base_url"

    fun init(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences("HttpClientSharedPref", 0)
    }

    fun setCustomHeaders(headers: Map<String, String>) {
        sharedPreferences?.apply {
            edit().putString(KEY_CUSTOM_HEADERS, gson.toJson(headers)).apply()
        }
    }

    fun getCustomHeadersString(): String? = sharedPreferences?.getString(KEY_CUSTOM_HEADERS, null)

    @Deprecated("Use baseUrlAdapterFactory and set to TripKitUI.buildTripKitConfig to set a custom base url")
    fun setCustomBaseUrl(url: String) {
        sharedPreferences?.apply {
            edit().putString(KEY_CUSTOM_BASE_URL, url).apply()
        }
    }

    @Deprecated("Use baseUrlAdapterFactory that you can set via TripKitUI.buildTripKitConfig")
    fun getCustomBaseUrl(): String? = sharedPreferences?.getString(KEY_CUSTOM_BASE_URL, null)
}