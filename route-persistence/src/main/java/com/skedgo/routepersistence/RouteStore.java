package com.skedgo.routepersistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.skedgo.android.common.model.TripGroup;

import java.util.List;
import java.util.concurrent.Callable;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import skedgo.sqlite.Cursors;

import static com.skedgo.routepersistence.RouteContract.ROUTES;
import static com.skedgo.routepersistence.TripGroupContract.TABLE_TRIP_GROUPS;

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

  public Observable<TripGroup> getTripGroupsByRouteIdAsync(@NonNull final String routeId) {
    return Observable.fromCallable(
        new Callable<Cursor>() {
          @Override public Cursor call() throws Exception {
            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Pair<String, String[]> stringPair = GroupQueries.hasRequestId(routeId);
            return database.query(RouteContract.ROUTES, null, stringPair.first, stringPair.second, null, null, null, null);
          }
        }
    ).flatMap(Cursors.flattenCursor())
        .map(new Func1<Cursor, String>() {
          @Override public String call(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(RouteContract.TRIP_GROUP_ID));
          }
        })
        .flatMap(new Func1<String, Observable<TripGroup>>() {
          @Override public Observable<TripGroup> call(String tripGroupId) {
            return tripGroupStore.getTripGroupById(tripGroupId);
          }
        });
  }

  @DebugLog private int delete(Pair<String, String[]> whereClause) {
    final SQLiteDatabase database = databaseHelper.getWritableDatabase();
    return database.delete(TABLE_TRIP_GROUPS, whereClause.first, whereClause.second);
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
