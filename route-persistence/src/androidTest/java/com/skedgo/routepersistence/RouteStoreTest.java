package com.skedgo.routepersistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.core.deps.guava.collect.Lists;
import androidx.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skedgo.android.common.model.GsonAdaptersBooking;
import com.skedgo.android.common.model.GsonAdaptersRealtimeAlert;
import com.skedgo.android.common.model.Location;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;
import skedgo.tripkit.routing.TripSegment;
import com.skedgo.android.common.util.LowercaseEnumTypeAdapterFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RouteStoreTest {
  private RouteDatabaseHelper databaseHelper;
  private RouteStore store;

  @Before public void before() {
    final Context context = InstrumentationRegistry.getTargetContext();
    databaseHelper = new RouteDatabaseHelper(context, RouteStoreTest.class.getSimpleName());
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new LocationTypeAdapterFactory())
        .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
        .registerTypeAdapterFactory(new GsonAdaptersBooking())
        .registerTypeAdapterFactory(new GsonAdaptersRealtimeAlert())
        .create();
    store = new RouteStore(databaseHelper, gson);
  }

  @After public void after() {
    InstrumentationRegistry.getTargetContext().deleteDatabase(databaseHelper.getDatabaseName());
  }

  @Test public void shouldQueryExactSavedGroups() {
    final TripGroup group = new TripGroup();
    final Trip trip = new Trip();
    final TripSegment segment = new TripSegment();
    segment.setFrom(new Location(1.0, 2.0));
    segment.setTo(new Location(3.0, 4.0));
    trip.setSegments(Lists.newArrayList(segment));
    group.setTrips(Lists.newArrayList(trip));

    final String requestId = UUID.randomUUID().toString();
    store.saveAsync(requestId, Collections.singletonList(group))
        .toBlocking().first();

    final List<TripGroup> groups = store.queryAsync(GroupQueries.hasRequestId(requestId))
        .toList()
        .toBlocking().first();
    assertEquals(1, groups.size());
    final TripGroup actualGroup = groups.get(0);
    assertEquals(group.uuid(), actualGroup.uuid());

    final ArrayList<Trip> actualTrips = actualGroup.getTrips();
    assertEquals(1, actualTrips.size());

    final Trip actualTrip = actualTrips.get(0);
    assertEquals(1, actualTrip.getSegments().size());
  }

  @Test public void groupDeletionShouldTriggerTripDeletionAndSegmentDeletion() {
    final TripGroup group = new TripGroup();
    final Trip trip = new Trip();
    final TripSegment segment = new TripSegment();
    segment.setFrom(new Location(1.0, 2.0));
    segment.setTo(new Location(3.0, 4.0));
    trip.setSegments(Lists.newArrayList(segment));
    group.setTrips(Lists.newArrayList(trip));

    final String requestId = UUID.randomUUID().toString();
    store.saveAsync(requestId, Collections.singletonList(group))
        .toBlocking().first();
    store.deleteAsync(WhereClauses.matchesUuidOf(group))
        .toBlocking().first();

    // Deleting a group should trigger deleting its trips too.
    final SQLiteDatabase database = databaseHelper.getReadableDatabase();
    final Cursor tripCursor = database.rawQuery("select * from trips", null);
    assertEquals(0, tripCursor.getCount());
    tripCursor.close();

    // Deleting a trip should trigger deleting its segments.
    final Cursor segmentCursor = database.rawQuery("select * from segments", null);
    assertEquals(0, segmentCursor.getCount());
    segmentCursor.close();
  }

  @Test public void shouldQueryExactGroupHavingGivenUuid() {
    final String requestId = UUID.randomUUID().toString();
    final TripGroup group0 = new TripGroup();
    {
      final Trip trip = new Trip();
      final TripSegment segment = new TripSegment();
      segment.setFrom(new Location(1.0, 2.0));
      segment.setTo(new Location(3.0, 4.0));
      trip.setSegments(Lists.newArrayList(segment));
      group0.setTrips(Lists.newArrayList(trip));
    }
    store.saveAsync(requestId, Collections.singletonList(group0))
        .toBlocking().first();

    final TripGroup group1 = new TripGroup();
    {
      final Trip trip = new Trip();
      final TripSegment segment = new TripSegment();
      segment.setFrom(new Location(1.0, 2.0));
      segment.setTo(new Location(3.0, 4.0));
      trip.setSegments(Lists.newArrayList(segment));
      group0.setTrips(Lists.newArrayList(trip));
    }
    store.saveAsync(requestId, Collections.singletonList(group1))
        .toBlocking().first();

    final TestSubscriber<TripGroup> subscriber = new TestSubscriber<>();
    store.queryAsync(GroupQueries.hasUuid(group0.uuid())).subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();
    assertEquals(1, subscriber.getValueCount());

    final TripGroup group = subscriber.getOnNextEvents().get(0);
    assertEquals(group0.uuid(), group.uuid());
  }

  @Test public void shouldClearOldTripsWhenUpdatingGroup() {
    final String requestId = UUID.randomUUID().toString();
    final Trip trip0 = new Trip();
    final TripGroup group0 = new TripGroup();
    {
      final TripSegment segment = new TripSegment();
      segment.setFrom(new Location(1.0, 2.0));
      segment.setTo(new Location(3.0, 4.0));
      trip0.setSegments(Lists.newArrayList(segment));
      group0.setTrips(Lists.newArrayList(trip0));
    }
    store.saveAsync(requestId, Collections.singletonList(group0))
        .toBlocking().first();

    final Trip trip1 = new Trip();
    {
      final TripSegment segment = new TripSegment();
      segment.setFrom(new Location(1.0, 2.0));
      segment.setTo(new Location(3.0, 4.0));
      trip1.setSegments(Lists.newArrayList(segment));
      group0.setTrips(Lists.newArrayList(trip1));
    }
    store.saveAsync(requestId, Collections.singletonList(group0))
        .toBlocking().first();

    final TripGroup actualGroup = store.queryAsync(GroupQueries.hasUuid(group0.uuid()))
        .toBlocking().first();

    final ArrayList<Trip> actualTrips = actualGroup.getTrips();
    assertEquals(1, actualTrips.size());

    final Trip actualTrip = actualTrips.get(0);
    assertEquals(trip1.uuid(), actualTrip.uuid());
    assertEquals(1, actualTrip.getSegments().size());
  }

  @Test public void shouldDeletePastTripThatHappenedBefore2HoursAgo() {
    // Given a trip that happened before 2 hours ago.
    final Trip trip = new Trip();
    trip.setStartTimeInSecs(0L);
    trip.setEndTimeInSecs(TimeUnit.HOURS.toSeconds(1L));
    final TripGroup group = new TripGroup();
    {
      final TripSegment segment = new TripSegment();
      segment.setFrom(new Location(1.0, 2.0));
      segment.setTo(new Location(3.0, 4.0));
      trip.setSegments(Lists.newArrayList(segment));
      group.setTrips(Lists.newArrayList(trip));
    }
    final String requestId = UUID.randomUUID().toString();
    store.saveAsync(requestId, Collections.singletonList(group))
        .toBlocking().first();

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .toBlocking().first();

    final long currentMillis = TimeUnit.HOURS.toMillis(4L);
    final int deletedCount = store.deleteAsync(WhereClauses.happenedBefore(
        2, // hours
        currentMillis
    )).toBlocking().first();
    assertEquals(1, deletedCount);

    final TestSubscriber<TripGroup> subscriber = new TestSubscriber<>();
    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .subscribe(subscriber);

    subscriber.awaitTerminalEvent();
    subscriber.assertNoValues();
  }

  @Test public void shouldNotDeletePastTripThatHappenedWithin2HoursAgo() {
    // Given a trip that happened within 2 hours ago.
    final Trip trip = new Trip();
    trip.setStartTimeInSecs(0L);
    trip.setEndTimeInSecs(TimeUnit.HOURS.toSeconds(3L));
    final TripGroup group = new TripGroup();
    {
      final TripSegment segment = new TripSegment();
      segment.setFrom(new Location(1.0, 2.0));
      segment.setTo(new Location(3.0, 4.0));
      trip.setSegments(Lists.newArrayList(segment));
      group.setTrips(Lists.newArrayList(trip));
    }
    final String requestId = UUID.randomUUID().toString();
    store.saveAsync(requestId, Collections.singletonList(group))
        .toBlocking().first();

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .toBlocking().first();

    final long currentMillis = TimeUnit.HOURS.toMillis(4L);
    final int deletedCount = store.deleteAsync(WhereClauses.happenedBefore(
        2, // hours
        currentMillis
    )).toBlocking().first();
    assertEquals(0, deletedCount);

    store.queryAsync(GroupQueries.hasUuid(group.uuid()))
        .toBlocking().first();
  }
}
