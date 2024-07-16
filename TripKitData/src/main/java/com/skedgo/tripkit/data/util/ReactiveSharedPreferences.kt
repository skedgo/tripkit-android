package com.skedgo.tripkit.data.util

import android.content.SharedPreferences
import io.reactivex.Observable

typealias PreferenceKey = String

fun SharedPreferences.onChanged(): Observable<PreferenceKey> = Observable
    .create {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, preferenceKey ->
            if (preferenceKey != null) {
                it.onNext(preferenceKey)
            }
        }
        registerOnSharedPreferenceChangeListener(listener)
        it.setCancellable { unregisterOnSharedPreferenceChangeListener(listener) }
    }
