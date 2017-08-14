package com.skedgo.routepersistence

import rx.Observable
import rx.functions.Func0
import rx.subjects.PublishSubject
import skedgo.tripkit.routing.TripGroup
import java.util.concurrent.ConcurrentHashMap

class RouteRepository constructor(
    private val routeStore: RouteStore,
    private val currentMillisProvider: Func0<Long>) {

  private val requestIdTripGroupIdCache: ConcurrentHashMap<String, List<String>> = ConcurrentHashMap()
  private val tripGroupCache: ConcurrentHashMap<String, TripGroup> = ConcurrentHashMap()
  private val onNewTripGroupsAvailable = PublishSubject.create<String>()

  fun getTripGroups(requestId: String): List<TripGroup> =
      if (requestIdTripGroupIdCache.contains(requestId)) {
        requestIdTripGroupIdCache[requestId]
            ?.filter { tripGroupCache.contains(it) }
            ?.map { tripGroupCache[it]!! }!!
      } else {
        routeStore.queryAsync(GroupQueries.hasRequestId(requestId))
            .toList()
            .doOnNext { addTripGroups(requestId, it) }
            .toBlocking()
            .first()
      }

  fun getTripGroup(uuid: String) = tripGroupCache[uuid]

  fun deletePastRoutesAsync(): Observable<Int> = routeStore.deleteAsync(
      WhereClauses.happenedBefore(
          3, /* hours */
          currentMillisProvider.call()
      ))

  fun addTripGroups(requestId: String, groups: List<TripGroup>) {
    requestIdTripGroupIdCache[requestId] = groups.map { tripGroup ->
      addTripGroup(tripGroup)
      tripGroup.uuid()
    }
  }

  fun addTripGroup(tripGroup: TripGroup) {
    tripGroupCache[tripGroup.uuid()] = tripGroup
  }

  fun onNewTripGroupsAvailable(): Observable<String> = onNewTripGroupsAvailable.asObservable()

}