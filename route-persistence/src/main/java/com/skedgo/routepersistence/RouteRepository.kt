package com.skedgo.routepersistence

import rx.Observable
import rx.functions.Func0
import rx.subjects.PublishSubject
import skedgo.tripkit.routing.TripGroup
import java.util.concurrent.ConcurrentHashMap

open class RouteRepository constructor(
    private val routeStore: RouteStore,
    private val currentMillisProvider: Func0<Long>) {

  internal var requestIdTripGroupIdCache: ConcurrentHashMap<String, MutableList<String>> = ConcurrentHashMap()
  internal var tripGroupCache: ConcurrentHashMap<String, TripGroup> = ConcurrentHashMap()
  private val onNewTripGroupsAvailable = PublishSubject.create<String>()

  open fun getTripGroups(requestId: String): List<TripGroup> =
      if (requestIdTripGroupIdCache.containsKey(requestId)) {
        requestIdTripGroupIdCache[requestId]
            ?.filter { tripGroupCache.containsKey(it) }
            ?.map { tripGroupCache[it]!! }!!
      } else {
        routeStore.queryAsync(GroupQueries.hasRequestId(requestId))
            .toList()
            .doOnNext { addTripGroups(requestId, it) }
            .toBlocking()
            .first()
      }

  open fun getTripGroup(uuid: String): TripGroup? = tripGroupCache[uuid]

  open fun deletePastRoutesAsync(): Observable<Int> = routeStore.deleteAsync(
      WhereClauses.happenedBefore(
          3, /* hours */
          currentMillisProvider.call()
      ))

  open fun addTripGroups(requestId: String, groups: List<TripGroup>) {
    val tripGroupIds = groups.map { tripGroup ->
      addTripGroup(tripGroup)
      tripGroup.uuid()
    }
    if (requestIdTripGroupIdCache[requestId] == null) {
      requestIdTripGroupIdCache[requestId] = mutableListOf()
    }
    requestIdTripGroupIdCache[requestId]!!.addAll(tripGroupIds)
    onNewTripGroupsAvailable.onNext(requestId)
  }

  open fun addTripGroup(tripGroup: TripGroup) {
    tripGroupCache[tripGroup.uuid()] = tripGroup
  }

  open fun onNewTripGroupsAvailable(): Observable<String> = onNewTripGroupsAvailable.asObservable()

}