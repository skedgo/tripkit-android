package com.skedgo.android.tripkit

import android.content.SharedPreferences
import rx.Observable
import rx.schedulers.Schedulers
import util.onChanged

internal const val KEY_IS_CONCESSION_PRICING_PREFERRED = "isConcessionPricingPreferred"
internal const val KEY_IS_WHEELCHAIR_PREFERRED = "isWheelchairPreferred"

class DefaultTripPreferences(
    private val preferences: SharedPreferences
) : TripPreferences {

  override fun isConcessionPricingPreferred(): Boolean =
      preferences.getBoolean(KEY_IS_CONCESSION_PRICING_PREFERRED, false)

  override fun setConcessionPricingPreferred(isConcessionPricingPreferred: Boolean) {
    preferences.edit()
        .putBoolean(KEY_IS_CONCESSION_PRICING_PREFERRED, isConcessionPricingPreferred)
        .apply()
  }

  override fun isWheelchairPreferred(): Boolean =
      preferences.getBoolean(KEY_IS_WHEELCHAIR_PREFERRED, false)

  override fun hasWheelchairInformation(): Observable<Boolean> =
      Observable
          .fromCallable { preferences.getBoolean(KEY_IS_WHEELCHAIR_PREFERRED, false) }
          .repeatWhen {
            preferences
                .onChanged()
                .filter { key -> key == KEY_IS_WHEELCHAIR_PREFERRED }
          }
          .subscribeOn(Schedulers.io())

  override fun setWheelchairPreferred(isWheelchairPreferred: Boolean) {
    preferences.edit()
        .putBoolean(KEY_IS_WHEELCHAIR_PREFERRED, isWheelchairPreferred)
        .apply()
  }
}