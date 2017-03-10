package skedgo.tripkit.validbookingcount.domain

import rx.Observable
import skedgo.tripkit.account.domain.HasUserToken
import javax.inject.Inject

open class GetRemoteValidBookingCount @Inject constructor(
    private val hasUserToken: HasUserToken,
    private val validBookingCountRepository: ValidBookingCountRepository
) {
  open fun execute(): Observable<Int>
      = hasUserToken.execute()
      .filter { it }
      .flatMap { validBookingCountRepository.getRemoteValidBookingCount() }
}
