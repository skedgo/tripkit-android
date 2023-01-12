package com.skedgo.tripkit.routing

import android.content.Context
import android.content.SharedPreferences

object GetOffAlertCache {

    private const val PREF_KEY = "KEY_GET_OFF_ALERTS"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    }

    fun setTripAlertOnState(tripUuid: String, onState: Boolean) {
        if (onState) {
            //As per adrian, only one trip can have alerts on, so will need to clear first to make sure no other trips has alerts on
            sharedPreferences.edit().apply {
                clear()
                putBoolean(tripUuid, onState).apply()
            }.apply()
            //sharedPreferences.edit().putBoolean(tripUuid, onState).apply()
        } else {
            sharedPreferences.edit().remove(tripUuid).apply()
        }
    }

    fun isTripAlertStateOn(tripUuid: String): Boolean = sharedPreferences.getBoolean(tripUuid, false)

}