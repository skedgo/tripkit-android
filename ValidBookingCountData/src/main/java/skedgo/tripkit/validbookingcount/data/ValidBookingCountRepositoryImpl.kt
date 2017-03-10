package skedgo.tripkit.validbookingcount.data

import android.content.SharedPreferences
import rx.Emitter
import rx.Observable
import skedgo.tripkit.validbookingcount.domain.ValidBookingCountRepository

internal class ValidBookingCountRepositoryImpl(
    private val api: ValidBookingCountApi,
    private val preferences: SharedPreferences
) : ValidBookingCountRepository {
  companion object {
    val KEY_VALID_BOOKING_COUNT = "validBookingCount"
  }

  override fun getLocalValidBookingCount(): Observable<Int>
      = Observable.fromEmitter<Int>({
    if (preferences.contains(KEY_VALID_BOOKING_COUNT)) {
      it.onNext(preferences.getInt(KEY_VALID_BOOKING_COUNT, -1))
    }
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
      if (key == KEY_VALID_BOOKING_COUNT) {
        it.onNext(prefs.getInt(KEY_VALID_BOOKING_COUNT, -1))
      }
    }
    preferences.registerOnSharedPreferenceChangeListener(listener)
    it.setCancellation { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
  }, Emitter.BackpressureMode.BUFFER)

  override fun getRemoteValidBookingCount(): Observable<Int>
      = api.fetchValidBookingCount()
      .map { it.count() }
      .doOnNext {
        preferences.edit()
            .putInt(KEY_VALID_BOOKING_COUNT, it)
            .apply()
      }
}
