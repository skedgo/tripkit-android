package util

import android.content.SharedPreferences
import rx.Emitter
import rx.Observable

typealias PreferenceKey = String

fun SharedPreferences.onChanged(): Observable<PreferenceKey> = Observable
    .create<PreferenceKey>({
      val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, preferenceKey ->
        it.onNext(preferenceKey)
      }
      registerOnSharedPreferenceChangeListener(listener)
      it.setCancellation { unregisterOnSharedPreferenceChangeListener(listener) }
    }, Emitter.BackpressureMode.BUFFER)
