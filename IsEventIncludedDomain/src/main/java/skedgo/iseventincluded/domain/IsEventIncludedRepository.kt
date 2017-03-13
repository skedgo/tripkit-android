package skedgo.iseventincluded.domain

import rx.Observable

interface IsEventIncludedRepository {
  fun setIsEventIncluded(eventId: String, isEventIncluded: IsEventIncluded): Observable<Unit>
  fun getIsEventIncluded(eventId: String): Observable<IsEventIncluded>
}
