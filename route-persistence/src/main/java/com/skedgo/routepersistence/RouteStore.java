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
  private TripGroupStore tripGroupStore;

  public RouteStore(SQLiteOpenHelper databaseHelper,
                    TripGroupStore tripGroupStore) {
    this.databaseHelper = databaseHelper;
    this.tripGroupStore = tripGroupStore;
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

  public Observable<TripGroup> getTripGroupsByRouteIdAsync(@NonNull String routeId) {
    return queryAsync(GroupQueries.hasRequestId(routeId));
  }

  @DebugLog private Observable<TripGroup> queryAsync(@NonNull Pair<String, String[]> query) {
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
    tripGroupStore.put(groups);
    for (TripGroup group : groups) {
      saveRoute(database, requestId, group.uuid());
    }
  }

  private void saveRoute(SQLiteDatabase database, String routeId, String groupId) {
    ContentValues values = new ContentValues(2);
    values.put(RouteContract.ROUTE_ID, routeId);
    values.put(RouteContract.TRIP_GROUP_ID, groupId);
    database.insertWithOnConflict(ROUTES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
  }
}
