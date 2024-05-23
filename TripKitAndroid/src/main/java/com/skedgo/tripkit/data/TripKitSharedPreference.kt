package com.skedgo.tripkit.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_CLIENT_FEATURES
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_CLIENT_ID
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_POLYGON
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_TRIP_KIT_LAT_LNG
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_NAME_TRIP_KIT
import com.skedgo.tripkit.account.data.Polygon
import com.skedgo.tripkit.common.util.TripKitLatLng
import com.skedgo.tripkit.extensions.fromJson
import javax.inject.Inject

// TODO update codes to use this for accessing TripKit SharedPreference
/**
 * Singleton class to centralize handling of TripKit local data using [SharedPreferences]
 */
@SuppressLint("StaticFieldLeak")
class TripKitSharedPreference @Inject constructor(context: Context) : BaseSharedPreference(context) {

    override val prefenceKey: String
        get() = PREF_NAME_TRIP_KIT

    fun saveClientId(clientId: String) {
        sharedPreferences.edit()
            .putString(PREF_KEY_CLIENT_ID, clientId)
            .apply()
    }

    fun getClientId(): String? =
        sharedPreferences.getString(PREF_KEY_CLIENT_ID, null)

    fun saveClientFeatures(features: List<String>) {
        sharedPreferences.edit()
            .putString(PREF_KEY_CLIENT_FEATURES, Gson().toJson(features))
            .apply()
    }

    fun getClientFeatures(): List<String> =
        Gson().fromJson(sharedPreferences.getString(PREF_KEY_CLIENT_FEATURES, null) ?: "")

    fun savePolygon(polygon: Polygon?) {
        sharedPreferences.edit()
            .putString(PREF_KEY_POLYGON, polygon?.let {
                gson.toJson(it)
            } ?: "")
            .apply()
    }

    fun getPolygon(): Polygon? {
        val dataString = sharedPreferences.getString(PREF_KEY_POLYGON, "") ?: ""
        return gson.fromJson(dataString)
    }
}