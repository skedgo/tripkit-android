package skedgo.tripkit.configuration.data

import android.content.SharedPreferences
import rx.Observable
import rx.schedulers.Schedulers
import skedgo.tripkit.domain.configuration.RegionEligibility
import skedgo.tripkit.domain.configuration.RegionEligibilityRepository

internal class RegionEligibilityRepositoryImpl constructor(
    private val preferences: SharedPreferences
) : RegionEligibilityRepository {
  companion object {
    val KEY_REGION_ELIGIBILITY = "regionEligibility"
  }

  override fun getRegionEligibility(): Observable<RegionEligibility>
      = Observable.fromCallable { preferences.getString(KEY_REGION_ELIGIBILITY, null) }
      .filter { it != null }
      .map(::RegionEligibility)
      .subscribeOn(Schedulers.io())

  override fun setRegionEligibility(regionEligibility: RegionEligibility): Observable<Unit>
      = Observable.fromCallable {
    preferences.edit()
        .putString(KEY_REGION_ELIGIBILITY, regionEligibility.value)
        .apply()
  }
}
