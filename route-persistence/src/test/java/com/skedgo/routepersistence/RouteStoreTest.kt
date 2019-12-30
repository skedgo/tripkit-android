package com.skedgo.routepersistence

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.GsonBuilder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.common.model.GsonAdaptersBooking
import com.skedgo.tripkit.common.model.GsonAdaptersRealtimeAlert
import com.skedgo.tripkit.common.model.Location
import com.skedgo.tripkit.common.util.LowercaseEnumTypeAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import com.skedgo.tripkit.routing.TripSegment
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class RouteStoreTest {
  private var databaseHelper: RouteDatabaseHelper? = null
  private lateinit var store: RouteStore

  @Before
  fun before() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    databaseHelper = RouteDatabaseHelper(context, RouteStoreTest::class.java.simpleName)
    val gson = GsonBuilder()
        .registerTypeAdapterFactory(LocationTypeAdapterFactory())
        .registerTypeAdapterFactory(LowercaseEnumTypeAdapterFactory())
        .registerTypeAdapterFactory(GsonAdaptersBooking())
        .registerTypeAdapterFactory(GsonAdaptersRealtimeAlert())
        .create()
    store = RouteStore(databaseHelper!!, gson)
  }


  @Test
  fun shouldQueryExactSavedGroups() {
    val group = TripGroup()
    val trip = Trip()
    val segment = TripSegment()
    segment.from = Location(1.0, 2.0)
    segment.to = Location(3.0, 4.0)
    trip.segments = arrayListOf(segment)
    group.trips = arrayListOf(trip)

    val requestId = UUID.randomUUID().toString()
    store.saveAsync(requestId, listOf(group))
        .blockingFirst()

    val groups = store.queryAsync(GroupQueries.hasRequestId(requestId))
        .toList()
        .blockingGet()
    assertEquals(1, groups.size.toLong())
    val actualGroup = groups[0]
    assertEquals(group.uuid(), actualGroup.uuid())

    val actualTrips = actualGroup.trips
    assertEquals(1, actualTrips!!.size.toLong())

    val actualTrip = actualTrips[0]
    assertEquals(1, actualTrip.segments.size.toLong())
  }

  @Test
  fun groupDeletionShouldTriggerTripDeletionAndSegmentDeletion() {
    val group = TripGroup()
    val trip = Trip()
    val segment = TripSegment()
    segment.from = Location(1.0, 2.0)
    segment.to = Location(3.0, 4.0)
    trip.segments = arrayListOf(segment)
    group.trips = arrayListOf(trip)

    val requestId = UUID.randomUUID().toString()
    store.saveAsync(requestId, listOf(group))
        .blockingFirst()
    store.deleteAsync(WhereClauses.matchesUuidOf(group))
        .blockingFirst()

    // Deleting a group should trigger deleting its trips too.
    val database = databaseHelper!!.readableDatabase
    val tripCursor = database.rawQuery("select * from trips", null)
    assertEquals(0, tripCursor.count.toLong())
    tripCursor.close()

    // Deleting a trip should trigger deleting its segments.
    val segmentCursor = database.rawQuery("select * from segments", null)
    assertEquals(0, segmentCursor.count.toLong())
    segmentCursor.close()
  }

  @Test
  fun shouldQueryExactGroupHavingGivenUuid() {
    val requestId = UUID.randomUUID().toString()
    val group2 = TripGroup()
    run {
      val trip = Trip()
      val segment = TripSegment()
      segment.from = Location(1.0, 2.0)
      segment.to = Location(3.0, 4.0)
      trip.segments = arrayListOf(segment)
      group2.trips = arrayListOf(trip)
    }
    var observer = store.saveAsync(requestId, listOf(group2))
        .test()
    observer.awaitTerminalEvent()
    observer.assertComplete()

    val group1 = TripGroup()
    run {
      val trip = Trip()
      val segment = TripSegment()
      segment.from = Location(1.0, 2.0)
      segment.to = Location(3.0, 4.0)
      trip.segments = arrayListOf(segment)
      group1.trips = arrayListOf(trip)
    }
    observer = store.saveAsync(requestId, listOf(group1))
        .test()
    observer.awaitTerminalEvent()
    observer.assertComplete()

    val subscriber = store.queryAsync(GroupQueries.hasUuid(group2.uuid())).test()
    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    assertEquals(1, subscriber.values().count().toLong())

    val group = subscriber.values().first()
    assertEquals(group2.uuid(), group.uuid())
  }

  @Test
  fun shouldClearOldTripsWhenUpdatingGroup() {
    val requestId = UUID.randomUUID().toString()
    val trip0 = Trip()
    val group0 = TripGroup()
    run {
      val segment = TripSegment()
      segment.from = Location(1.0, 2.0)
      segment.to = Location(3.0, 4.0)
      trip0.segments = arrayListOf(segment)
      group0.trips = arrayListOf(trip0)
    }
    store.saveAsync(requestId, listOf(group0))
        .blockingFirst()
    val trip1 = Trip()
    run {
      val segment = TripSegment()
      segment.from = Location(1.0, 2.0)
      segment.to = Location(3.0, 4.0)
      trip1.segments = arrayListOf(segment)
      group0.trips = arrayListOf(trip1)
    }
    store.saveAsync(requestId, listOf(group0))
        .blockingFirst()

    val actualGroup = store.queryAsync(GroupQueries.hasUuid(group0.uuid()))
        .blockingFirst()

    val actualTrips = actualGroup.trips
    assertEquals(1, actualTrips!!.size.toLong())

    val actualTrip = actualTrips[0]
    assertEquals(trip1.uuid(), actualTrip.uuid())
    assertEquals(1, actualTrip.segments.size.toLong())
  }

  @Test
  fun shouldDeletePastTripThatHappenedBefore2HoursAgo() {
    // Given a trip that happened before 2 hours ago.
    val trip = Trip()
    trip.startTimeInSecs = 0L
    trip.endTimeInSecs = TimeUnit.HOURS.toSeconds(1L)
    val group = TripGroup()
    run {
      val segment = TripSegment()
      segment.from = Location(1.0, 2.0)
      segment.to = Location(3.0, 4.0)
      trip.segments = arrayListOf(segment)
      group.trips = arrayListOf(trip)
    }
    val requestId = UUID.randomUUID().toString()
    store.saveAsync(requestId, listOf(group))
        .blockingFirst()

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .blockingFirst()

    val currentMillis = TimeUnit.HOURS.toMillis(4L)
    val deletedCount = store.deleteAsync(WhereClauses.happenedBefore(
        2, // hours
        currentMillis
    )).blockingFirst()
    assertEquals(1, deletedCount.toLong())

    val subscriber = store.queryAsync(GroupQueries.hasUuid(group.uuid())).test()
    subscriber.awaitTerminalEvent()
    subscriber.assertNoValues()
  }

  @Test
  fun `should add trip as display Trip`() {
    val trip = mock<Trip>().apply {
      whenever(this.uuid()).thenReturn("b")
      whenever(this.id).thenReturn(1)
    }
    val group = mock<TripGroup>().apply {
      whenever(this.uuid()).thenReturn("a")
      whenever(this.displayTrip).thenReturn(trip)
      whenever(this.trips).thenReturn(arrayListOf(trip))
    }
    group.displayTripId = 1
    group.trips = arrayListOf(trip)

    var observer = store.saveAsync(null, listOf(group))
        .test()
    observer.awaitTerminalEvent()
    observer.assertComplete()

    val newDisplayTrip = Trip().apply {
      this.uuid("c")
      this.id = 10
    }
    var observer2 = store.addTripToTripGroup("a", newDisplayTrip)
        .test()
    observer2.awaitTerminalEvent()
    observer2.assertComplete()

    var observer3 = store.queryAsync(GroupQueries.hasUuid("a"))
        .test()

    observer3.awaitTerminalEvent()
    observer3.assertComplete().assertValueCount(1)
        .values().first()
        .let {
          it.displayTripId `should equal` 2
          it.trips!!.map { it.uuid() } `should contain all` (listOf("b", "c"))
          it.displayTrip!!.uuid() `should equal` "c"
        }
  }

  @Test
  fun shouldNotDeletePastTripThatHappenedWithin2HoursAgo() {
    // Given a trip that happened within 2 hours ago.
    val trip = Trip()
    trip.startTimeInSecs = 0L
    trip.endTimeInSecs = TimeUnit.HOURS.toSeconds(3L)
    val group = TripGroup()
    run {
      val segment = TripSegment()
      segment.from = Location(1.0, 2.0)
      segment.to = Location(3.0, 4.0)
      trip.segments = arrayListOf(segment)
      group.trips = arrayListOf(trip)
    }
    val requestId = UUID.randomUUID().toString()
    store.saveAsync(requestId, listOf(group))
        .blockingFirst()

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .blockingFirst()

    val currentMillis = TimeUnit.HOURS.toMillis(4L)
    val deletedCount = store.deleteAsync(WhereClauses.happenedBefore(
        2, // hours
        currentMillis
    )).blockingFirst()
    assertEquals(0, deletedCount.toLong())

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .blockingFirst()
  }
}
