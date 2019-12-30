package com.skedgo.tripkit.routingstatus

import io.reactivex.Completable
import io.reactivex.Observable

interface RoutingStatusRepository {
  fun getRoutingStatus(requestId: String): Observable<RoutingStatus>
  fun putRoutingStatus(routingStatus: RoutingStatus): Completable
}
