package com.skedgo.android.tripkit

import android.content.SharedPreferences
import rx.Observable
import rx.schedulers.Schedulers
import util.onChanged

internal const val concessionPricingPrefKey = "isConcessionPricingPreferred"
internal const val wheelchairPrefKey = "isWheelchairPreferred"

class DefaultTripPreferences(
    private val preferences: SharedPreferences
) : TripPreferences {
  override fun isConcessionPricingPreferred(): Boolean =
      preferences.getBoolean(concessionPricingPrefKey, false)

  override fun whenConcessionPricingPreferenceChanges(): Observable<Boolean> =
      preferences
          .onChanged()
          .filter { it == concessionPricingPrefKey }
          .map {
            preferences.getBoolean(concessionPricingPrefKey, false)
          }
          .subscribeOn(Schedulers.io())

  override fun setConcessionPricingPreferred(isConcessionPricingPreferred: Boolean) {
    preferences.edit()
        .putBoolean(concessionPricingPrefKey, isConcessionPricingPreferred)
        .apply()
  }

  override fun isWheelchairPreferred(): Boolean =
      preferences.getBoolean(wheelchairPrefKey, false)

  override fun whenWheelchairPreferenceChanges(): Observable<Boolean> =
      preferences
          .onChanged()
          .filter { it == wheelchairPrefKey }
          .map {
            preferences.getBoolean(wheelchairPrefKey, false)
          }
          .subscribeOn(Schedulers.io())

  override fun setWheelchairPreferred(isWheelchairPreferred: Boolean) {
    preferences.edit()
        .putBoolean(wheelchairPrefKey, isWheelchairPreferred)
        .apply()
  }
}