package skedgo.tripkit.validbookingcount.domain

import rx.Observable
import javax.inject.Inject

open class HasValidBookingCount @Inject constructor(
    private val validBookingCountRepository: ValidBookingCountRepository
) {
  open fun execute(): Observable<Boolean>
      = validBookingCountRepository.getLocalValidBookingCount()
      .map { it > 0 }
}
