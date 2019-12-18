package skedgo.tripkit.validbookingcount.data

import android.content.SharedPreferences
import io.reactivex.BackpressureStrategy
import io.reactivex.Emitter
import io.reactivex.Flowable
import io.reactivex.Observable
import skedgo.tripkit.validbookingcount.domain.ValidBookingCountRepository

internal class ValidBookingCountRepositoryImpl(
    private val api: ValidBookingCountApi,
    private val preferences: SharedPreferences
) : ValidBookingCountRepository {
  companion object {
    val KEY_VALID_BOOKING_COUNT = "validBookingCount"
  }

  override fun getLocalValidBookingCount(): Observable<Int>
      = Flowable.create<Int>({
    if (preferences.contains(KEY_VALID_BOOKING_COUNT)) {
      it.onNext(preferences.getInt(KEY_VALID_BOOKING_COUNT, -1))
    }
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
      if (key == KEY_VALID_BOOKING_COUNT) {
        it.onNext(prefs.getInt(KEY_VALID_BOOKING_COUNT, -1))
      }
    }
    preferences.registerOnSharedPreferenceChangeListener(listener)
    it.setCancellable { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
  }, BackpressureStrategy.BUFFER).toObservable()

  override fun getRemoteValidBookingCount(): Observable<Int>
      = api.fetchValidBookingCount()
      .map { it.count() }
      .doOnNext {
        preferences.edit()
            .putInt(KEY_VALID_BOOKING_COUNT, it)
            .apply()
      }
}
