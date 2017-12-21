package skedgo.tripkit.routingstatus

import rx.Completable
import rx.Observable

interface RoutingStatusRepository {
  fun getRoutingStatus(requestId: String): Observable<RoutingStatus>
  fun putRoutingStatus(routingStatus: RoutingStatus): Completable
}
