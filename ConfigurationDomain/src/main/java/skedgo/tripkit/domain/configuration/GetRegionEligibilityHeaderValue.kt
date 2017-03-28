package skedgo.tripkit.domain.configuration

import rx.Observable
import javax.inject.Inject

open class GetRegionEligibilityHeaderValue @Inject constructor(
    private val regionEligibilityRepository: RegionEligibilityRepository
) {
  open fun execute(defaultRegionEligibility: RegionEligibility): Observable<String>
      = regionEligibilityRepository.getRegionEligibility()
      .defaultIfEmpty(defaultRegionEligibility)
      .map { it.value }
}
