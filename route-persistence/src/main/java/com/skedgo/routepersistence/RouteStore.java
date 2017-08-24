package com.skedgo.routepersistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.Gson;

import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;
import skedgo.tripkit.routing.TripSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.skedgo.routepersistence.RouteContract.COL_ARRIVE;
import static com.skedgo.routepersistence.RouteContract.COL_CALORIES_COST;
import static com.skedgo.routepersistence.RouteContract.COL_CARBON_COST;
import static com.skedgo.routepersistence.RouteContract.COL_CURRENCY_SYMBOL;
import static com.skedgo.routepersistence.RouteContract.COL_DEPART;
import static com.skedgo.routepersistence.RouteContract.COL_DISPLAY_TRIP_ID;
import static com.skedgo.routepersistence.RouteContract.COL_FREQUENCY;
import static com.skedgo.routepersistence.RouteContract.COL_GROUP_ID;
import static com.skedgo.routepersistence.RouteContract.COL_HASSLE_COST;
import static com.skedgo.routepersistence.RouteContract.COL_ID;
import static com.skedgo.routepersistence.RouteContract.COL_IS_NOTIFIABLE;
import static com.skedgo.routepersistence.RouteContract.COL_JSON;
import static com.skedgo.routepersistence.RouteContract.COL_MONEY_COST;
import static com.skedgo.routepersistence.RouteContract.COL_PLANNED_URL;
import static com.skedgo.routepersistence.RouteContract.COL_PROGRESS_URL;
import static com.skedgo.routepersistence.RouteContract.COL_QUERY_IS_LEAVE_AFTER;
import static com.skedgo.routepersistence.RouteContract.COL_REQUEST_ID;
import static com.skedgo.routepersistence.RouteContract.COL_SAVE_URL;
import static com.skedgo.routepersistence.RouteContract.COL_TEMP_URL;
import static com.skedgo.routepersistence.RouteContract.COL_TRIP_ID;
import static com.skedgo.routepersistence.RouteContract.COL_UPDATE_URL;
import static com.skedgo.routepersistence.RouteContract.COL_UUID;
import static com.skedgo.routepersistence.RouteContract.COL_WEIGHTED_SCORE;
import static com.skedgo.routepersistence.RouteContract.SELECT_SEGMENTS;
import static com.skedgo.routepersistence.RouteContract.SELECT_TRIPS;
import static com.skedgo.routepersistence.RouteContract.TABLE_SEGMENTS;
import static com.skedgo.routepersistence.RouteContract.TABLE_TRIPS;
import static com.skedgo.routepersistence.RouteContract.TABLE_TRIP_GROUPS;

public class RouteStore {
  private final SQLiteOpenHelper databaseHelper;
  private final Gson gson;

  public RouteStore(SQLiteOpenHelper databaseHelper, Gson gson) {
    this.databaseHelper = databaseHelper;
    this.gson = gson;
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

  public Observable<List<TripGroup>> updateAsync(final List<TripGroup> groups) {
    return Observable.fromCallable(new Callable<List<TripGroup>>() {
      @Override public List<TripGroup> call() throws Exception {
        final SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.beginTransaction();
        try {
          for (TripGroup group : groups) {
            final ContentValues values = new ContentValues();
            values.put(COL_DISPLAY_TRIP_ID, group.getDisplayTripId());
            database.update(TABLE_TRIP_GROUPS, values, COL_UUID + "= ?", new String[] {group.uuid()});
            database.delete(TABLE_SEGMENTS, COL_TRIP_ID + " IN (SELECT " + COL_TRIP_ID + " FROM " + TABLE_TRIPS + " WHERE " + COL_GROUP_ID + " = ?)", new String[] {group.uuid()});
            database.delete(TABLE_TRIPS, COL_GROUP_ID + " = ?", new String[] {group.uuid()});
            saveTrips(database, group.uuid(), group.getTrips());
          }
          database.setTransactionSuccessful();
        } finally {
          database.endTransaction();
        }
        return groups;
      }
    });
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
    final String json = cursor.getString(cursor.getColumnIndex(COL_JSON));
    return gson.fromJson(json, TripSegment.class);
  }

  private Trip asTrip(Cursor cursor) {
    final long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
    final String uuid = cursor.getString(cursor.getColumnIndex(COL_UUID));
    final String currencySymbol = cursor.getString(cursor.getColumnIndex(COL_CURRENCY_SYMBOL));
    final String saveUrl = cursor.getString(cursor.getColumnIndex(COL_SAVE_URL));
    final long depart = cursor.getLong(cursor.getColumnIndex(COL_DEPART));
    final long arrive = cursor.getLong(cursor.getColumnIndex(COL_ARRIVE));
    final float caloriesCost = cursor.getFloat(cursor.getColumnIndex(COL_CALORIES_COST));
    final float moneyCost = cursor.getFloat(cursor.getColumnIndex(COL_MONEY_COST));
    final float carbonCost = cursor.getFloat(cursor.getColumnIndex(COL_CARBON_COST));
    final float hassleCost = cursor.getFloat(cursor.getColumnIndex(COL_HASSLE_COST));
    final float weightedScore = cursor.getFloat(cursor.getColumnIndex(COL_WEIGHTED_SCORE));
    final String updateUrl = cursor.getString(cursor.getColumnIndex(COL_UPDATE_URL));
    final String progressUrl = cursor.getString(cursor.getColumnIndex(COL_PROGRESS_URL));
    final String plannedUrl = cursor.getString(cursor.getColumnIndex(COL_PLANNED_URL));
    final String tempUrl = cursor.getString(cursor.getColumnIndex(COL_TEMP_URL));
    final int queryIsLeaveAfter = cursor.getInt(cursor.getColumnIndex(COL_QUERY_IS_LEAVE_AFTER));

    final Trip trip = new Trip();
    trip.setId(id);
    trip.uuid(uuid);
    trip.setCurrencySymbol(currencySymbol);
    trip.setSaveURL(saveUrl);
    trip.setStartTimeInSecs(depart);
    trip.setEndTimeInSecs(arrive);
    trip.setCaloriesCost(caloriesCost);
    trip.setMoneyCost(moneyCost);
    trip.setCarbonCost(carbonCost);
    trip.setHassleCost(hassleCost);
    trip.setWeightedScore(weightedScore);
    trip.setUpdateURL(updateUrl);
    trip.setProgressURL(progressUrl);
    trip.setPlannedURL(plannedUrl);
    trip.setTemporaryURL(tempUrl);
    trip.setQueryIsLeaveAfter(queryIsLeaveAfter == 1);
    return trip;
  }

  private TripGroup asTripGroup(Cursor cursor) {
    final String uuid = cursor.getString(cursor.getColumnIndex(COL_UUID));
    final int frequency = cursor.getInt(cursor.getColumnIndex(COL_FREQUENCY));
    final long displayTripId = cursor.getLong(cursor.getColumnIndex(COL_DISPLAY_TRIP_ID));

    final TripGroup group = new TripGroup();
    group.uuid(uuid);
    group.setFrequency(frequency);
    group.setDisplayTripId(displayTripId);
    return group;
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

    final ContentValues values = new ContentValues();
    values.put(COL_UUID, group.uuid());
    values.put(COL_FREQUENCY, group.getFrequency());
    values.put(COL_DISPLAY_TRIP_ID, group.getDisplayTripId());
    values.put(COL_REQUEST_ID, requestId);
    values.put(COL_IS_NOTIFIABLE, isNotifiable ? 1 : 0);
    database.insertWithOnConflict(TABLE_TRIP_GROUPS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
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
    final ContentValues values = new ContentValues();
    values.put(COL_ID, trip.getId());
    values.put(COL_UUID, trip.uuid());
    values.put(COL_GROUP_ID, groupId);
    values.put(COL_CURRENCY_SYMBOL, trip.getCurrencySymbol());
    values.put(COL_SAVE_URL, trip.getSaveURL());
    values.put(COL_DEPART, trip.getStartTimeInSecs());
    values.put(COL_ARRIVE, trip.getEndTimeInSecs());
    values.put(COL_CALORIES_COST, trip.getCaloriesCost());
    values.put(COL_MONEY_COST, trip.getMoneyCost());
    values.put(COL_CARBON_COST, trip.getCarbonCost());
    values.put(COL_HASSLE_COST, trip.getHassleCost());
    values.put(COL_WEIGHTED_SCORE, trip.getWeightedScore());
    values.put(COL_UPDATE_URL, trip.getUpdateURL());
    values.put(COL_PROGRESS_URL, trip.getProgressURL());
    values.put(COL_PLANNED_URL, trip.getPlannedURL());
    values.put(COL_TEMP_URL, trip.getTemporaryURL());
    values.put(COL_QUERY_IS_LEAVE_AFTER, trip.queryIsLeaveAfter() ? 1 : 0);
    database.insertWithOnConflict(TABLE_TRIPS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
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
    final ContentValues values = new ContentValues();
    values.put(COL_TRIP_ID, tripId);
    values.put(COL_JSON, gson.toJson(segment, TripSegment.class));
    database.insertWithOnConflict(TABLE_SEGMENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }
}
