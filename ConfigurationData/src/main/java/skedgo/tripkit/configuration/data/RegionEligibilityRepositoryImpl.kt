package skedgo.tripkit.configuration.data

import android.content.SharedPreferences
import rx.Observable
import rx.schedulers.Schedulers
import skedgo.tripkit.configuration.domain.RegionEligibility
import skedgo.tripkit.configuration.domain.RegionEligibilityRepository

internal class RegionEligibilityRepositoryImpl constructor(
    private val preferences: SharedPreferences
) : RegionEligibilityRepository {
  val regionEligibilityKey = "regionEligibility"

  override fun getRegionEligibility(): Observable<RegionEligibility>
      = Observable.fromCallable { preferences.getString(regionEligibilityKey, null) }
      .filter { it != null }
      .map(::RegionEligibility)
      .subscribeOn(Schedulers.io())

  override fun setRegionEligibility(regionEligibility: RegionEligibility): Observable<Unit>
      = Observable.fromCallable {
    preferences.edit()
        .putString(regionEligibilityKey, regionEligibility.value)
        .apply()
  }
}
