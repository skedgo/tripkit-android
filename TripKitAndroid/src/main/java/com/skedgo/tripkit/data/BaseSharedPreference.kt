package com.skedgo.tripkit.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

abstract class BaseSharedPreference(context: Context) {

    abstract val prefenceKey: String

    protected val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(prefenceKey, Context.MODE_PRIVATE)
    }

    protected val gson: Gson by lazy { Gson() }

    fun saveData(data: Map<String, Any>) {
        sharedPreferences.edit().apply {
            data.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                }
            }
        }.apply()
    }
}