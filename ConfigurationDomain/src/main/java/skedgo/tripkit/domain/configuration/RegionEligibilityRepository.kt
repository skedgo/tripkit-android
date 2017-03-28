package skedgo.tripkit.domain.configuration

import rx.Observable

interface RegionEligibilityRepository {
  fun getRegionEligibility(): Observable<RegionEligibility>
  fun setRegionEligibility(regionEligibility: RegionEligibility): Observable<Unit>
}
