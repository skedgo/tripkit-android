package skedgo.iseventincluded.domain

import rx.Observable
import javax.inject.Inject

open class IsEventIncludedInTrips @Inject constructor(
    private val isEventIncludedRepository: IsEventIncludedRepository
) {
  open fun execute(eventId: String): Observable<Boolean>
      = isEventIncludedRepository.getIsEventIncluded(eventId)
      .map {
        when (it) {
          IsEventIncluded.NOT_SPECIFIED_YET -> true
          IsEventIncluded.YES -> true
          else -> false
        }
      }
}
