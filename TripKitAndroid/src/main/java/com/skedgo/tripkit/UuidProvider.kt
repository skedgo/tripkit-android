package com.skedgo.tripkit

import android.content.SharedPreferences
import java.util.UUID
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Named

internal class UuidProvider @Inject constructor(@param:Named("TripKitPrefs") private val preferences: SharedPreferences) :
    Callable<String> {
    @Synchronized
    override fun call(): String {
        val uuid = preferences.getString(KEY_UUID, null)
        if (uuid != null) {
            return uuid
        } else {
            val newUuid = UUID.randomUUID().toString()
            preferences.edit().putString(KEY_UUID, newUuid).apply()
            return newUuid
        }
    }

    companion object {
        private const val KEY_UUID = "UUID"
    }
}