package com.skedgo.routepersistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.Gson;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import skedgo.sqlite.SQLiteEntityAdapter;

import static com.skedgo.routepersistence.RouteContract.ROUTES;
import static com.skedgo.routepersistence.TripGroupContract.*;

public class RouteStore {
  private final SQLiteOpenHelper databaseHelper;
  private final Gson gson;
  private final SQLiteEntityAdapter<TripGroup> tripGroupEntityAdapter;
  private final SQLiteEntityAdapter<Trip> tripEntityAdapter;
  private final SQLiteEntityAdapter<TripSegment> tripSegmentEntityAdapter;

  public RouteStore(SQLiteOpenHelper databaseHelper,
                    Gson gson,
                    SQLiteEntityAdapter<TripGroup> tripGroupEntityAdapter,
                    SQLiteEntityAdapter<Trip> tripEntityAdapter,
                    SQLiteEntityAdapter<TripSegment> tripSegmentEntityAdapter) {
    this.databaseHelper = databaseHelper;
    this.gson = gson;
    this.tripGroupEntityAdapter = tripGroupEntityAdapter;
    this.tripEntityAdapter = tripEntityAdapter;
    this.tripSegmentEntityAdapter = tripSegmentEntityAdapter;
  }

  public Observable<Integer> deleteAsync(final Pair<String, String[]> whereClause) {
    return Observable
        .fromCallable(new Callable<Integer>() {
          @Override public Integer call() throws Exception {
            return delete(whereClause);
          }
        })
        .subscribeOn(Schedulers.io());
  }

  public Observable<List<TripGroup>> saveAsync(final String requestId, final List<TripGroup> groups) {
    return Observable
        .fromCallable(new Callable<List<TripGroup>>() {
          @Override public List<TripGroup> call() throws Exception {
            saveTripGroupsInTransaction(requestId, groups);
            return groups;
          }
        })
        .subscribeOn(Schedulers.io());
  }

  @DebugLog public Observable<TripGroup> queryAsync(@NonNull Pair<String, String[]> query) {
    return queryAsync(query.first, query.second);
  }

  @DebugLog private int delete(Pair<String, String[]> whereClause) {
    final SQLiteDatabase database = databaseHelper.getWritableDatabase();
    return database.delete(TABLE_TRIP_GROUPS, whereClause.first, whereClause.second);
  }

  private Observable<TripGroup> queryAsync(
      final String selection,
      final String[] selectionArgs) {
    return Observable
        .create(new Observable.OnSubscribe<TripGroup>() {
          @Override public void call(Subscriber<? super TripGroup> subscriber) {
            final SQLiteDatabase database = databaseHelper.getReadableDatabase();
            queryInTransaction(subscriber, database, selection, selectionArgs);
            subscriber.onCompleted();
          }
        })
        .subscribeOn(Schedulers.io());
  }

  @DebugLog private void queryInTransaction(
      Subscriber<? super TripGroup> subscriber,
      SQLiteDatabase database,
      String selection,
      String[] selectionArgs) {
    database.beginTransaction();
    try {
      query(subscriber, database, selection, selectionArgs);
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }
  }

  private void query(
      Subscriber<? super TripGroup> subscriber,
      SQLiteDatabase database,
      String selection,
      String[] selectionArgs) {
    final Cursor groupCursor = database.rawQuery(selection, selectionArgs);
    try {
      while (!subscriber.isUnsubscribed() && groupCursor.moveToNext()) {
        final TripGroup group = asTripGroup(groupCursor);
        final Cursor tripCursor = database.rawQuery(SELECT_TRIPS, new String[] {group.uuid()});
        final ArrayList<Trip> trips = new ArrayList<>(tripCursor.getCount());
        try {
          while (!subscriber.isUnsubscribed() && tripCursor.moveToNext()) {
            final Trip trip = asTrip(tripCursor);
            final Cursor segmentCursor = database.rawQuery(SELECT_SEGMENTS, new String[] {trip.uuid()});
            final ArrayList<TripSegment> segments = asSegments(subscriber, segmentCursor);
            trip.setSegments(segments);
            trips.add(trip);
          }
        } finally {
          if (!tripCursor.isClosed()) {
            tripCursor.close();
          }
        }
        group.setTrips(trips);
        subscriber.onNext(group);
      }
    } finally {
      if (!groupCursor.isClosed()) {
        groupCursor.close();
      }
    }
  }

  private ArrayList<TripSegment> asSegments(
      Subscriber<? super TripGroup> subscriber,
      Cursor segmentCursor) {
    final ArrayList<TripSegment> segments = new ArrayList<>(segmentCursor.getCount());
    try {
      while (!subscriber.isUnsubscribed() && segmentCursor.moveToNext()) {
        final TripSegment segment = asSegment(segmentCursor);
        segments.add(segment);
      }
    } finally {
      if (!segmentCursor.isClosed()) {
        segmentCursor.close();
      }
    }
    return segments;
  }

  private TripSegment asSegment(Cursor cursor) {
    return tripSegmentEntityAdapter.toEntity(cursor);
  }

  private Trip asTrip(Cursor cursor) {
    return tripEntityAdapter.toEntity(cursor);
  }

  private TripGroup asTripGroup(Cursor cursor) {
    return tripGroupEntityAdapter.toEntity(cursor);
  }

  @DebugLog private void saveTripGroupsInTransaction(
      String requestId,
      List<TripGroup> groups) {
    final SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    try {
      saveTripGroups(database, requestId, groups);
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }
  }

  private void saveTripGroups(
      SQLiteDatabase database,
      String requestId,
      List<TripGroup> groups) {
    for (int i = 0, size = groups.size(); i < size; i++) {
      final TripGroup group = groups.get(i);
      saveTripGroupAndTrips(database, requestId, group);
    }
  }

  private void saveTripGroupAndTrips(
      SQLiteDatabase database,
      String requestId,
      TripGroup group) {
    final Trip trip = group.getDisplayTrip();
    saveTripGroup(database, requestId, group, trip.isFavourite());
    saveTrips(database, group.uuid(), group.getTrips());
  }

  @DebugLog private void saveTripGroup(
      SQLiteDatabase database,
      String requestId,
      TripGroup group,
      boolean isNotifiable) {
    database.delete(TABLE_TRIP_GROUPS, COL_UUID + " = ?", new String[] {group.uuid()});

    ContentValues values = tripGroupEntityAdapter.toContentValues(group);
    values.put(COL_IS_NOTIFIABLE, isNotifiable ? 1 : 0);
    database.insertWithOnConflict(TABLE_TRIP_GROUPS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    saveRoute(database, requestId, group.uuid());
  }

  private void saveRoute(SQLiteDatabase database, String routeId, String groupId) {
    ContentValues values = new ContentValues(2);
    values.put(RouteContract.ROUTE_ID, routeId);
    values.put(RouteContract.TRIP_GROUP_ID, groupId);
    database.insertWithOnConflict(ROUTES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }

  private void saveTrips(SQLiteDatabase database, String groupId, List<Trip> trips) {
    if (trips != null) {
      for (int i = 0, size = trips.size(); i < size; i++) {
        final Trip trip = trips.get(i);
        saveTripAndSegments(database, groupId, trip);
      }
    }
  }

  private void saveTripAndSegments(SQLiteDatabase database, String groupId, Trip trip) {
    saveTrip(database, groupId, trip);
    saveSegments(database, trip.uuid(), trip.getSegments());
  }

  @DebugLog private void saveTrip(
      SQLiteDatabase database,
      String groupId,
      Trip trip) {
    ContentValues tripValues = tripEntityAdapter.toContentValues(trip);
    tripValues.put(COL_GROUP_ID, groupId);
    database.insertWithOnConflict(TABLE_TRIPS, null, tripValues, SQLiteDatabase.CONFLICT_REPLACE);
  }

  @DebugLog
  private void saveSegments(
      SQLiteDatabase database,
      String tripId,
      List<TripSegment> segments) {
    if (segments != null) {
      for (int i = 0, size = segments.size(); i < size; i++) {
        final TripSegment segment = segments.get(i);
        saveSegment(database, tripId, segment);
      }
    }
  }

  private void saveSegment(
      SQLiteDatabase database,
      String tripId,
      TripSegment segment) {
    ContentValues values = tripSegmentEntityAdapter.toContentValues(segment);
    values.put(COL_TRIP_ID, tripId);
    database.insertWithOnConflict(TABLE_SEGMENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }
}
