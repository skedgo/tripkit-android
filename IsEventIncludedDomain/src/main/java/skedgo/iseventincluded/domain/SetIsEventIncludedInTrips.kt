package skedgo.iseventincluded.domain

import rx.Observable
import javax.inject.Inject

open class SetIsEventIncludedInTrips @Inject constructor(
    private val isEventIncludedRepository: IsEventIncludedRepository
) {
  open fun call(eventId: String, isIncluded: Boolean): Observable<Unit>
      = Observable.just(isIncluded)
      .map { if (it) IsEventIncluded.YES else IsEventIncluded.NO }
      .flatMap { isEventIncludedRepository.setIsEventIncluded(eventId, it) }
}
