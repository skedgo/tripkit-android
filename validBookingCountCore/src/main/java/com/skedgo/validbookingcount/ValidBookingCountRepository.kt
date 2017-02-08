package com.skedgo.validbookingcount

import android.content.SharedPreferences
import rx.Emitter
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named


private const val KEY_VALID_BOOKING_COUNT = "validBookingCount"

class ValidBookingCountRepository @Inject internal constructor(@Named("ValidBookingCountPreferences") val sharedPreferences: SharedPreferences,
                                                               private val api: ValidBookingCountApi) {


  fun validBookingCount(): Observable<Int> {
    return Observable.fromEmitter<Int>({
      if (sharedPreferences.contains(KEY_VALID_BOOKING_COUNT)) {
        it.onNext(sharedPreferences.getInt(KEY_VALID_BOOKING_COUNT, -1))
      }
      val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == KEY_VALID_BOOKING_COUNT) {
          it.onNext(sharedPreferences.getInt(KEY_VALID_BOOKING_COUNT, -1))
        }
      }
      sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
      it.setCancellation { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }, Emitter.BackpressureMode.BUFFER)
        .subscribeOn(Schedulers.io())
  }

  fun updateValidBookingCount(): Observable<Int> {
    return api.fetchValidBookingsCountAsync()
        .map { it.count() }
        .observeOn(Schedulers.io())
        .doOnNext {
          sharedPreferences.edit()
              .putInt(KEY_VALID_BOOKING_COUNT, it)
              .commit()
        }
  }
}