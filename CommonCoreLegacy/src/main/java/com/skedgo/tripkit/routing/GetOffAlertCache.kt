package com.skedgo.tripkit.routing

import android.content.Context
import android.content.SharedPreferences


object GetOffAlertCache {

    private const val PREF_KEY = "KEY_GET_OFF_ALERTS"
    private const val PREF_KEY_TRIP_ID = "KEY_TRIP_ID"
    private const val PREF_KEY_TRIP_GROUP_ID = "KEY_TRIP_GROUP_ID"
    private lateinit var sharedPreferences: SharedPreferences
    var onTripAlertStateChanged: () -> Unit = {}

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    }

    fun setTripAlertOnState(tripUuid: String, tripGroupId: String, onState: Boolean) {
        if (onState) {
            //As per adrian, only one trip can have alerts on, so will need to clear first to make sure no other trips has alerts on
            sharedPreferences.edit().apply {
                clear()
                putBoolean(tripUuid, onState)
                putString(PREF_KEY_TRIP_ID, tripUuid)
                putString(PREF_KEY_TRIP_GROUP_ID, tripGroupId)
            }.apply()
            onTripAlertStateChanged.invoke()
        } else {
            sharedPreferences.edit().apply {
                remove(tripUuid)
                remove(PREF_KEY_TRIP_ID)
                remove(PREF_KEY_TRIP_GROUP_ID)
            }.apply()
            onTripAlertStateChanged.invoke()
        }
    }

    fun isTripAlertStateOn(tripUuid: String): Boolean =
        sharedPreferences.getBoolean(tripUuid, false)

    fun getTripId(): String = sharedPreferences.getString(PREF_KEY_TRIP_ID, "").orEmpty()

    fun getTripAlertGroupId(): String? = sharedPreferences.getString(PREF_KEY_TRIP_GROUP_ID, null)

}