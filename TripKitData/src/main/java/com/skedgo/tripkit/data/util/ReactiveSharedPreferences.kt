package com.skedgo.tripkit.data.util

import android.content.SharedPreferences
import io.reactivex.Observable

typealias PreferenceKey = String

fun SharedPreferences.onChanged(): Observable<PreferenceKey> = Observable
    .create { emitter ->
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, preferenceKey ->
            preferenceKey?.let { emitter.onNext(it) }
        }
        registerOnSharedPreferenceChangeListener(listener)
        emitter.setCancellable { unregisterOnSharedPreferenceChangeListener(listener) }
    }
