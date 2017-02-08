package com.skedgo.validbookingcount

import android.content.SharedPreferences
import rx.Emitter
import rx.Observable
import javax.inject.Inject
import javax.inject.Named


private const val KEY_VALID_BOOKING_COUNT = "validBookingCount"

class ValidBookingCountRepository @Inject internal constructor(
    @Named("ValidBookingCountPreferences") val preferences: SharedPreferences,
    private val api: ValidBookingCountApi
) {
  fun validBookingCount(): Observable<Int> {
    return Observable.fromEmitter<Int>({
      if (preferences.contains(KEY_VALID_BOOKING_COUNT)) {
        it.onNext(preferences.getInt(KEY_VALID_BOOKING_COUNT, -1))
      }
      val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == KEY_VALID_BOOKING_COUNT) {
          it.onNext(sharedPreferences.getInt(KEY_VALID_BOOKING_COUNT, -1))
        }
      }
      preferences.registerOnSharedPreferenceChangeListener(listener)
      it.setCancellation { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }, Emitter.BackpressureMode.BUFFER)
  }

  fun updateValidBookingCount(): Observable<Int> {
    return api.fetchValidBookingsCountAsync()
        .map { it.count() }
        .doOnNext {
          preferences.edit()
              .putInt(KEY_VALID_BOOKING_COUNT, it)
              .apply()
        }
  }
}
