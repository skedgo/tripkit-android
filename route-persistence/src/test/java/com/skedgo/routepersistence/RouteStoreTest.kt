package com.skedgo.routepersistence

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.GsonBuilder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.android.common.model.GsonAdaptersBooking
import com.skedgo.android.common.model.GsonAdaptersRealtimeAlert
import com.skedgo.android.common.model.Location
import com.skedgo.android.common.util.LowercaseEnumTypeAdapterFactory
import org.amshove.kluent.`should be in`
import org.amshove.kluent.`should contain all`
import org.amshove.kluent.`should equal`
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.ext.junit.runners.AndroidJUnit4
import rx.observers.TestSubscriber
import skedgo.tripkit.routing.Trip
import skedgo.tripkit.routing.TripGroup
import skedgo.tripkit.routing.TripSegment
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
        .toBlocking().first()

    val groups = store.queryAsync(GroupQueries.hasRequestId(requestId))
        .toList()
        .toBlocking().first()
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
        .toBlocking().first()
    store.deleteAsync(WhereClauses.matchesUuidOf(group))
        .toBlocking().first()

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
    store.saveAsync(requestId, listOf(group2))
        .test()
        .awaitTerminalEvent()
        .assertCompleted()

    val group1 = TripGroup()
    run {
      val trip = Trip()
      val segment = TripSegment()
      segment.from = Location(1.0, 2.0)
      segment.to = Location(3.0, 4.0)
      trip.segments = arrayListOf(segment)
      group1.trips = arrayListOf(trip)
    }
    store.saveAsync(requestId, listOf(group1))
        .test()
        .awaitTerminalEvent()
        .assertCompleted()

    val subscriber = TestSubscriber<TripGroup>()
    store.queryAsync(GroupQueries.hasUuid(group2.uuid())).subscribe(subscriber)

    subscriber.awaitTerminalEvent()
    subscriber.assertNoErrors()
    assertEquals(1, subscriber.valueCount.toLong())

    val group = subscriber.onNextEvents[0]
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
        .toBlocking().first()

    val trip1 = Trip()
    run {
      val segment = TripSegment()
      segment.from = Location(1.0, 2.0)
      segment.to = Location(3.0, 4.0)
      trip1.segments = arrayListOf(segment)
      group0.trips = arrayListOf(trip1)
    }
    store.saveAsync(requestId, listOf(group0))
        .toBlocking().first()

    val actualGroup = store.queryAsync(GroupQueries.hasUuid(group0.uuid()))
        .toBlocking().first()

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
        .toBlocking().first()

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .toBlocking().first()

    val currentMillis = TimeUnit.HOURS.toMillis(4L)
    val deletedCount = store.deleteAsync(WhereClauses.happenedBefore(
        2, // hours
        currentMillis
    )).toBlocking().first()
    assertEquals(1, deletedCount.toLong())

    val subscriber = TestSubscriber<TripGroup>()
    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .subscribe(subscriber)

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

    store.saveAsync(null, listOf(group))
        .test()
        .awaitTerminalEvent()
        .assertCompleted()

    val newDisplayTrip = Trip().apply {
      this.uuid("c")
      this.id = 10
    }
    store.addTripToTripGroup("a", newDisplayTrip)
        .test()
        .awaitTerminalEvent()
        .assertCompleted()

    store.queryAsync(GroupQueries.hasUuid("a"))
        .test()
        .awaitTerminalEvent()
        .assertCompleted()
        .assertValueCount(1)
        .onNextEvents.first()
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
        .toBlocking().first()

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .toBlocking().first()

    val currentMillis = TimeUnit.HOURS.toMillis(4L)
    val deletedCount = store.deleteAsync(WhereClauses.happenedBefore(
        2, // hours
        currentMillis
    )).toBlocking().first()
    assertEquals(0, deletedCount.toLong())

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .toBlocking().first()
  }
}
