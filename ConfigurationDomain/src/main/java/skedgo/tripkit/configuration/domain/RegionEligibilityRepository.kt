package skedgo.tripkit.configuration.domain

import rx.Observable

interface RegionEligibilityRepository {
  fun getRegionEligibility(): Observable<RegionEligibility>
  fun setRegionEligibility(regionEligibility: RegionEligibility): Observable<Unit>
}
