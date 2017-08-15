package com.skedgo.routepersistence

import android.util.Pair
import com.nhaarman.mockito_kotlin.mock
import org.amshove.kluent.*
import org.junit.Test
import org.mockito.ArgumentMatchers
import rx.Observable
import rx.functions.Func0
import skedgo.tripkit.routing.TripGroup
import org.mockito.Mockito.any


import java.util.concurrent.ConcurrentHashMap

@Suppress("IllegalIdentifier")
class RouteRepository2Test {

  val routeStore: RouteStore = mock()
  val currentMillisProvider: Func0<Long> = mock()
  val routeRepository: RouteRepository by lazy {
    RouteRepository(routeStore, currentMillisProvider)
  }

  @Test fun `should get cached tripGroups`() {

    val requests: ConcurrentHashMap<String, MutableList<String>> = ConcurrentHashMap()
    val tripGroups: ConcurrentHashMap<String, TripGroup> = ConcurrentHashMap()
    val tripGroup: TripGroup = mock()

    requests["request id"] = mutableListOf("tripGroup id")
    tripGroups["tripGroup id"] = tripGroup

    routeRepository.requestIdTripGroupIdCache = requests
    routeRepository.tripGroupCache = tripGroups

    val groups = routeRepository.getTripGroups("request id")

    tripGroup `should be in` groups
  }

  @Test fun `should get cached tripGroup`() {

    val tripGroups: ConcurrentHashMap<String, TripGroup> = mock()
    val tripGroup: TripGroup = mock()

    When calling tripGroups["tripGroup id"] itReturns tripGroup

    routeRepository.tripGroupCache = tripGroups

    val saveTripGroup = routeRepository.getTripGroup("tripGroup id")

    tripGroup `should be` saveTripGroup
  }

  @Test fun `should add tripGroups`() {

    val tripGroup: TripGroup = mock()
    When calling tripGroup.uuid() itReturns "tripGroup id"

    routeRepository.addTripGroups("request id", listOf(tripGroup))

    "tripGroup id" `should be in` routeRepository.requestIdTripGroupIdCache["request id"].orEmpty()
    tripGroup `should be` routeRepository.tripGroupCache["tripGroup id"]
  }

  @Test fun `should add tripGroup`() {

    val tripGroup: TripGroup = mock()
    When calling tripGroup.uuid() itReturns "tripGroup id"

    routeRepository.addTripGroup(tripGroup)

    tripGroup `should be` routeRepository.tripGroupCache["tripGroup id"]
  }
}