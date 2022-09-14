package com.skedgo.tripkit.data.util

import android.content.SharedPreferences
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

typealias PreferenceKey = String

fun SharedPreferences.onChanged(): Observable<PreferenceKey> = Observable
    .create<PreferenceKey> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, preferenceKey ->
            it.onNext(preferenceKey)
        }
        registerOnSharedPreferenceChangeListener(listener)
        it.setCancellable { unregisterOnSharedPreferenceChangeListener(listener) }
    }
