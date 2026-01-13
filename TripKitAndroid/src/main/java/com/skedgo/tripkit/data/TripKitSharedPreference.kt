package com.skedgo.tripkit.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_CLIENT
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_CLIENT_FEATURES
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_CLIENT_ID
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_KEY_POLYGON
import com.skedgo.tripkit.TripKitConstants.Companion.PREF_NAME_TRIP_KIT
import com.skedgo.tripkit.account.data.Client
import com.skedgo.tripkit.account.data.Polygon
import com.skedgo.tripkit.extensions.fromJson
import javax.inject.Inject

// TODO update codes to use this for accessing TripKit SharedPreference
/**
 * Singleton class to centralize handling of TripKit local data using [SharedPreferences]
 */
@SuppressLint("StaticFieldLeak")
class TripKitSharedPreference @Inject constructor(context: Context) :
    BaseSharedPreference(context) {

    override val prefenceKey: String
        get() = PREF_NAME_TRIP_KIT

    fun saveClientId(clientId: String) {
        sharedPreferences.edit()
            .putString(PREF_KEY_CLIENT_ID, clientId)
            .apply()
    }

    fun getClientId(): String? =
        sharedPreferences.getString(PREF_KEY_CLIENT_ID, null)

    fun saveClient(client: Client) {
        sharedPreferences.edit()
            .putString(PREF_KEY_CLIENT, Gson().toJson(client))
            .apply()
    }

    fun getClient(): Client? =
        sharedPreferences.getString(PREF_KEY_CLIENT, null)
            ?.takeIf { it.isNotBlank() }
            ?.let { Gson().fromJson<Client>(it) }

    fun saveClientFeatures(features: List<String>) {
        sharedPreferences.edit()
            .putString(PREF_KEY_CLIENT_FEATURES, Gson().toJson(features))
            .apply()
    }

    fun getClientFeatures(): List<String> =
        sharedPreferences.getString(PREF_KEY_CLIENT_FEATURES, null)
            ?.takeIf { it.isNotBlank() }
            ?.let { Gson().fromJson<List<String>>(it) }
            ?: emptyList()

    fun savePolygon(polygon: Polygon?) {
        sharedPreferences.edit()
            .putString(PREF_KEY_POLYGON, polygon?.let {
                gson.toJson(it)
            } ?: "")
            .apply()
    }

    fun getPolygon(): Polygon? {
        val dataString = sharedPreferences.getString(PREF_KEY_POLYGON, null)
        return dataString?.takeIf { it.isNotBlank() }?.let { gson.fromJson<Polygon>(it) }
    }
}