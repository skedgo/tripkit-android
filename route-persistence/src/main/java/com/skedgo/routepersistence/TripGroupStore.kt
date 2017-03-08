package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Pair
import com.skedgo.android.common.model.Trip
import com.skedgo.android.common.model.TripGroup
import com.skedgo.android.common.model.TripSegment
import com.skedgo.routepersistence.TripGroupContract.SELECT_SEGMENTS
import com.skedgo.routepersistence.TripGroupContract.SELECT_TRIPS
import hugo.weaving.DebugLog
import rx.Observable
import rx.Subscriber
import rx.schedulers.Schedulers
import java.util.*


class TripGroupStore constructor(val routeDatabaseHelper: RouteDatabaseHelper,
                                          private val tripGroupEntityAdapter: TripGroupEntityAdapter,
                                          private val tripEntityAdapter: TripEntityAdapter,
                                          private val tripSegmentEntityAdapter: TripSegmentEntityAdapter) {

  fun getTripGroupById(uuid: String): Observable<TripGroup> {
    return queryAsync(GroupQueries.hasUuid(uuid))
  }

  @DebugLog fun queryAsync(query: Pair<String, Array<String>>): Observable<TripGroup> {
    return queryAsync(query.first, query.second)
  }

  private fun queryAsync(
      selection: String,
      selectionArgs: Array<String>): Observable<TripGroup> {
    return Observable.fromCallable {
      val database = routeDatabaseHelper.readableDatabase
      val groupCursor = database.query(TripGroupContract.TABLE_TRIP_GROUPS, null, selection, selectionArgs, null, null, null, null)
      val groups = ArrayList<TripGroup>(groupCursor.count)
      while (groupCursor.moveToNext()) {
        val group = asTripGroup(groupCursor)
        val tripCursor = database.rawQuery(SELECT_TRIPS, arrayOf<String>(group.uuid()))
        val trips = ArrayList<Trip>(tripCursor.count)
        try {
          while (tripCursor.moveToNext()) {
            val trip = asTrip(tripCursor)
            val segmentCursor = database.rawQuery(SELECT_SEGMENTS, arrayOf<String>(trip.uuid()))
            val segments = asSegments(segmentCursor)
            trip.segments = segments
            trips.add(trip)
          }
        } finally {
          if (!tripCursor.isClosed) {
            tripCursor.close()
          }
        }
        group.trips = trips
        groups.add(group)
      }
      groups
    }.flatMap { Observable.from(it) }
  }

  private fun asSegments(
      segmentCursor: Cursor): ArrayList<TripSegment> {
    val segments = ArrayList<TripSegment>(segmentCursor.count)
    try {
      while (segmentCursor.moveToNext()) {
        val segment = asSegment(segmentCursor)
        segments.add(segment)
      }
    } finally {
      if (!segmentCursor.isClosed) {
        segmentCursor.close()
      }
    }
    return segments
  }

  private fun asSegment(cursor: Cursor): TripSegment {
    return tripSegmentEntityAdapter.toEntity(cursor)
  }

  private fun asTrip(cursor: Cursor): Trip {
    return tripEntityAdapter.toEntity(cursor)
  }

  private fun asTripGroup(cursor: Cursor): TripGroup {
    return tripGroupEntityAdapter.toEntity(cursor)
  }

  fun put(tripGroups: List<TripGroup>) {
    val routeDb = routeDatabaseHelper.writableDatabase
    routeDb.beginTransaction()
    try {
      tripGroups.forEach { group ->
        val isNotifiable = group.displayTrip?.isFavourite ?: false
        group.trips?.forEach { trip ->
          routeDb.insertWithOnConflict(TripGroupContract.TABLE_TRIPS, null, tripEntityAdapter.toContentValues(trip, group.uuid()), SQLiteDatabase.CONFLICT_REPLACE)
          trip.segments.forEach { segment ->
            routeDb.insertWithOnConflict(TripGroupContract.TABLE_SEGMENTS, null, tripSegmentEntityAdapter.toContentValues(segment, trip.uuid()), SQLiteDatabase.CONFLICT_REPLACE)
          }
        }
        routeDb.insertWithOnConflict(TripGroupContract.TABLE_TRIP_GROUPS, null, tripGroupEntityAdapter.toContentValues(group, isNotifiable), SQLiteDatabase.CONFLICT_REPLACE)
      }
      routeDb.setTransactionSuccessful()
    } finally {
      routeDb.endTransaction()
    }
  }

}
