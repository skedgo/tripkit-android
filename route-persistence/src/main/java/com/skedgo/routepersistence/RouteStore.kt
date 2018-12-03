package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Pair
import com.google.gson.Gson
import com.skedgo.routepersistence.RouteContract.*
import hugo.weaving.DebugLog
import rx.Completable
import rx.Observable
import rx.schedulers.Schedulers
import rx.schedulers.Schedulers.io
import skedgo.sqlite.Cursors.flattenCursor
import skedgo.tripkit.routing.Source
import skedgo.tripkit.routing.Trip
import skedgo.tripkit.routing.TripGroup
import skedgo.tripkit.routing.TripSegment
import java.util.*

open class RouteStore(private val databaseHelper: SQLiteOpenHelper, private val gson: Gson) {

  open fun deleteAsync(whereClause: Pair<String, Array<String>>): Observable<Int> {
    return Observable
        .fromCallable { delete(whereClause) }
        .subscribeOn(io())
  }

  open fun updateAlternativeTrips(groups: List<TripGroup>): Observable<List<TripGroup>> {
    return Observable.fromCallable {
      val database = databaseHelper.writableDatabase
      database.beginTransaction()
      try {
        for (group in groups) {
          val values = ContentValues()
          values.put(COL_DISPLAY_TRIP_ID, group.displayTripId)
          database.update(TABLE_TRIP_GROUPS, values, "$COL_UUID= ?", arrayOf(group.uuid()))
        }
        database.setTransactionSuccessful()
      } finally {
        database.endTransaction()
      }
      groups
    }
  }

  open fun updateNotifiability(groupId: String, isFavorite: Boolean): Completable {
    return Completable.fromAction {
      val database = databaseHelper.writableDatabase
      val values = ContentValues()
      values.put(COL_IS_NOTIFIABLE, if (isFavorite) 1 else 0)
      database.update(TABLE_TRIP_GROUPS, values, "$COL_UUID= ?", arrayOf(groupId))
    }
  }

  open fun saveAsync(requestId: String?, groups: List<TripGroup>): Observable<List<TripGroup>> {
    return Observable
        .fromCallable {
          saveTripGroupsInTransaction(requestId, groups)
          groups
        }
        .subscribeOn(io())
  }

  @DebugLog
  open fun queryAsync(query: Pair<String, Array<String>?>): Observable<TripGroup> {
    return queryAsync(query.first, query.second)
  }

  open fun queryTripGroupIdsByRequestIdAsync(requestId: String): Observable<String> {
    return Observable
        .fromCallable {
          val database = databaseHelper.readableDatabase
          database.query(TABLE_TRIP_GROUPS,
              arrayOf(COL_UUID),
              "$COL_REQUEST_ID =?",
              arrayOf(requestId), null, null, null)
        }
        .flatMap(flattenCursor())
        .map { cursor -> cursor.getString(cursor.getColumnIndex(COL_UUID)) }
  }

  open fun updateTripAsync(oldTripUuid: String, trip: Trip): Completable {
    return Completable
        .fromAction {
          val database = databaseHelper.writableDatabase
          val values = ContentValues()
          values.put(COL_DEPART, trip.startTimeInSecs)
          values.put(COL_ARRIVE, trip.endTimeInSecs)
          values.put(COL_SAVE_URL, trip.saveURL)
          values.put(COL_UPDATE_URL, trip.updateURL)
          values.put(COL_PROGRESS_URL, trip.progressURL)
          values.put(COL_CARBON_COST, trip.carbonCost)
          values.put(COL_MONEY_COST, trip.moneyCost)
          values.put(COL_HASSLE_COST, trip.hassleCost)

          database.beginTransaction()
          try {
            database.update(TABLE_TRIPS, values, "$COL_UUID = ?", arrayOf(oldTripUuid))
            database.delete(TABLE_SEGMENTS, "$COL_TRIP_ID = ?", arrayOf(oldTripUuid))
            saveSegments(database, oldTripUuid, trip.segments)
            database.setTransactionSuccessful()
          } finally {
            database.endTransaction()
          }
        }.subscribeOn(io())
  }

  open fun addTripToTripGroup(tripGroupId: String, displayTrip: Trip): Completable {
    return Completable
        .fromAction {
          val database = databaseHelper.writableDatabase
          database.beginTransaction()
          try {
            val cursor = database.rawQuery("SELECT MAX($COL_ID) FROM $TABLE_TRIPS WHERE $COL_GROUP_ID = ?", arrayOf(tripGroupId))
            cursor.moveToFirst()
            val newTripId = cursor.getLong(cursor.getColumnIndex("MAX($COL_ID)")) + 1
            cursor.close()
            displayTrip.id = newTripId
            saveTripAndSegments(database, tripGroupId, displayTrip)
            val values = ContentValues()
            values.put(COL_DISPLAY_TRIP_ID, newTripId)
            database.update(TABLE_TRIP_GROUPS, values, "$COL_UUID= ?", arrayOf(tripGroupId))
            database.setTransactionSuccessful()
          } finally {
            database.endTransaction()
          }
        }
        .subscribeOn(Schedulers.io())
  }

  @DebugLog
  private fun delete(whereClause: Pair<String, Array<String>>): Int {
    val database = databaseHelper.writableDatabase
    return database.delete(TABLE_TRIP_GROUPS, whereClause.first, whereClause.second)
  }

  private fun queryAsync(
      selection: String,
      selectionArgs: Array<String>?): Observable<TripGroup> {
    return Observable
        .fromCallable {
          val database = databaseHelper.readableDatabase
          database.rawQuery(selection, selectionArgs)
        }
        .flatMap(flattenCursor())
        .flatMap { groupCursor -> queryTripsOfTripGroup(groupCursor) }
        .subscribeOn(io())
  }

  private fun queryTripsOfTripGroup(groupCursor: Cursor): Observable<TripGroup> {
    val group = asTripGroup(groupCursor)
    return Observable
        .fromCallable {
          val database = databaseHelper.readableDatabase
          database.rawQuery(SELECT_TRIPS, arrayOf(group.uuid()))
        }
        .flatMap(flattenCursor())
        .flatMap { tripCursor -> querySegmentsOfTrip(tripCursor, groupCursor) }
        .toList()
        .map { trips ->
          group.trips = ArrayList(trips)
          group
        }
  }

  private fun querySegmentsOfTrip(tripCursor: Cursor, groupCursor: Cursor): Observable<Trip> {
    val trip = asTrip(tripCursor, groupCursor)
    return Observable
        .fromCallable {
          val database = databaseHelper.readableDatabase
          val segmentsCursor = database.rawQuery(SELECT_SEGMENTS, arrayOf(trip.uuid()))
          asSegments(segmentsCursor)
        }
        .map { tripSegments ->
          trip.segments = tripSegments
          trip
        }
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
    val json = cursor.getString(cursor.getColumnIndex(COL_JSON))
    return gson.fromJson(json, TripSegment::class.java)
  }

  private fun asTrip(tripCursor: Cursor, groupCursor: Cursor): Trip {
    val id = tripCursor.getLong(tripCursor.getColumnIndex(COL_ID))
    val uuid = tripCursor.getString(tripCursor.getColumnIndex(COL_UUID))
    val currencySymbol = tripCursor.getString(tripCursor.getColumnIndex(COL_CURRENCY_SYMBOL))
    val saveUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_SAVE_URL))
    val depart = tripCursor.getLong(tripCursor.getColumnIndex(COL_DEPART))
    val arrive = tripCursor.getLong(tripCursor.getColumnIndex(COL_ARRIVE))
    val caloriesCost = tripCursor.getFloat(tripCursor.getColumnIndex(COL_CALORIES_COST))
    val moneyCost = tripCursor.getFloat(tripCursor.getColumnIndex(COL_MONEY_COST))
    val carbonCost = tripCursor.getFloat(tripCursor.getColumnIndex(COL_CARBON_COST))
    val hassleCost = tripCursor.getFloat(tripCursor.getColumnIndex(COL_HASSLE_COST))
    val weightedScore = tripCursor.getFloat(tripCursor.getColumnIndex(COL_WEIGHTED_SCORE))
    val updateUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_UPDATE_URL))
    val progressUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_PROGRESS_URL))
    val plannedUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_PLANNED_URL))
    val tempUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_TEMP_URL))
    val queryIsLeaveAfter = tripCursor.getInt(tripCursor.getColumnIndex(COL_QUERY_IS_LEAVE_AFTER))

    val isNotifable = groupCursor.getInt(groupCursor.getColumnIndex(COL_IS_NOTIFIABLE)) == 1

    val trip = Trip()
    trip.id = id
    trip.uuid(uuid)
    trip.currencySymbol = currencySymbol
    trip.saveURL = saveUrl
    trip.startTimeInSecs = depart
    trip.endTimeInSecs = arrive
    trip.caloriesCost = caloriesCost
    trip.moneyCost = moneyCost
    trip.carbonCost = carbonCost
    trip.hassleCost = hassleCost
    trip.weightedScore = weightedScore
    trip.updateURL = updateUrl
    trip.progressURL = progressUrl
    trip.plannedURL = plannedUrl
    trip.temporaryURL = tempUrl
    trip.setQueryIsLeaveAfter(queryIsLeaveAfter == 1)
    trip.isFavourite(isNotifable)
    return trip
  }

  private fun asTripGroup(cursor: Cursor): TripGroup {
    val uuid = cursor.getString(cursor.getColumnIndex(COL_UUID))
    val frequency = cursor.getInt(cursor.getColumnIndex(COL_FREQUENCY))
    val displayTripId = cursor.getLong(cursor.getColumnIndex(COL_DISPLAY_TRIP_ID))
    val sources = gson.fromJson(cursor.getString(cursor.getColumnIndex(COL_SOURCES)), Array<Source>::class.java)
    val group = TripGroup()
    group.uuid(uuid)
    group.frequency = frequency
    group.displayTripId = displayTripId
    if (sources != null) {
      group.sources = Arrays.asList(*sources)
    }
    return group
  }

  @DebugLog
  private fun saveTripGroupsInTransaction(
      requestId: String?,
      groups: List<TripGroup>) {
    val database = databaseHelper.writableDatabase
    database.beginTransaction()
    try {
      saveTripGroups(database, requestId, groups)
      database.setTransactionSuccessful()
    } finally {
      database.endTransaction()
    }
  }

  private fun saveTripGroups(
      database: SQLiteDatabase,
      requestId: String?,
      groups: List<TripGroup>) {
    var i = 0
    val size = groups.size
    while (i < size) {
      val group = groups[i]
      saveTripGroupAndTrips(database, requestId, group)
      i++
    }
  }

  private fun saveTripGroupAndTrips(
      database: SQLiteDatabase,
      requestId: String?,
      group: TripGroup) {
    val trip = group.displayTrip
    saveTripGroup(database, requestId, group, trip!!.isFavourite)
    saveTrips(database, group.uuid(), group.trips)
  }

  @DebugLog
  private fun saveTripGroup(
      database: SQLiteDatabase,
      requestId: String?,
      group: TripGroup,
      isNotifiable: Boolean) {
    database.delete(TABLE_TRIP_GROUPS, "$COL_UUID = ?", arrayOf(group.uuid()))

    val values = ContentValues()
    values.put(COL_UUID, group.uuid())
    values.put(COL_FREQUENCY, group.frequency)
    values.put(COL_DISPLAY_TRIP_ID, group.displayTripId)
    values.put(COL_REQUEST_ID, requestId)
    values.put(COL_IS_NOTIFIABLE, if (isNotifiable) 1 else 0)
    if (group.sources != null) {
      values.put(COL_SOURCES, gson.toJson(group.sources!!.toTypedArray(), Array<Source>::class.java))
    }
    database.insertWithOnConflict(TABLE_TRIP_GROUPS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
  }

  private fun saveTrips(database: SQLiteDatabase, groupId: String, trips: List<Trip>?) {
    if (trips != null) {
      var i = 0
      val size = trips.size
      while (i < size) {
        val trip = trips[i]
        saveTripAndSegments(database, groupId, trip)
        i++
      }
    }
  }

  private fun saveTripAndSegments(database: SQLiteDatabase, groupId: String, trip: Trip) {
    saveTrip(database, groupId, trip)
    saveSegments(database, trip.uuid(), trip.segments)
  }

  @DebugLog
  private fun saveTrip(
      database: SQLiteDatabase,
      groupId: String,
      trip: Trip) {
    val values = ContentValues()
    values.put(COL_ID, trip.id)
    values.put(COL_UUID, trip.uuid())
    values.put(COL_GROUP_ID, groupId)
    values.put(COL_CURRENCY_SYMBOL, trip.currencySymbol)
    values.put(COL_SAVE_URL, trip.saveURL)
    values.put(COL_DEPART, trip.startTimeInSecs)
    values.put(COL_ARRIVE, trip.endTimeInSecs)
    values.put(COL_CALORIES_COST, trip.caloriesCost)
    values.put(COL_MONEY_COST, trip.moneyCost)
    values.put(COL_CARBON_COST, trip.carbonCost)
    values.put(COL_HASSLE_COST, trip.hassleCost)
    values.put(COL_WEIGHTED_SCORE, trip.weightedScore)
    values.put(COL_UPDATE_URL, trip.updateURL)
    values.put(COL_PROGRESS_URL, trip.progressURL)
    values.put(COL_PLANNED_URL, trip.plannedURL)
    values.put(COL_TEMP_URL, trip.temporaryURL)
    values.put(COL_QUERY_IS_LEAVE_AFTER, if (trip.queryIsLeaveAfter()) 1 else 0)
    database.insertWithOnConflict(TABLE_TRIPS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
  }

  @DebugLog
  private fun saveSegments(
      database: SQLiteDatabase,
      tripId: String,
      segments: List<TripSegment>?) {
    if (segments != null) {
      var i = 0
      val size = segments.size
      while (i < size) {
        val segment = segments[i]
        saveSegment(database, tripId, segment)
        i++
      }
    }
  }

  private fun saveSegment(
      database: SQLiteDatabase,
      tripId: String,
      segment: TripSegment) {
    val values = ContentValues()
    values.put(COL_TRIP_ID, tripId)
    values.put(COL_JSON, gson.toJson(segment, TripSegment::class.java))
    database.insertWithOnConflict(TABLE_SEGMENTS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
  }
}
