package com.skedgo.tripkit

import android.content.SharedPreferences
import androidx.collection.ArrayMap
import java.util.Collections

class DefaultCo2Preferences
/**
 * @param preferences This [SharedPreferences] should only be used to store CO2 profile.
 */(private val preferences: SharedPreferences) : Co2Preferences {
    override fun getCo2Profile(): Map<String, Float> {
        val all = preferences.all
        val co2Profile: MutableMap<String, Float> = ArrayMap(all.size)
        for ((key, value) in all) {
            co2Profile[key] = value as Float
        }
        return Collections.unmodifiableMap(co2Profile)
    }

    override fun setEmissions(modeId: String, gramsCO2PerKm: Float) {
        preferences.edit().putFloat(modeId, gramsCO2PerKm).apply()
    }
}