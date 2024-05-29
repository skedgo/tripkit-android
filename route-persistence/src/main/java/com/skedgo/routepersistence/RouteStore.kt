package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Pair
import com.google.gson.Gson
import com.skedgo.routepersistence.RouteContract.*
import com.skedgo.sqlite.Cursors.flattenCursor
import com.skedgo.tripkit.common.BuildConfig
import com.skedgo.tripkit.routing.Source
import com.skedgo.tripkit.routing.Trip
import com.skedgo.tripkit.routing.TripGroup
import com.skedgo.tripkit.routing.TripSegment
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

// TODO refactor and use Room
open class RouteStore(private val databaseHelper: SQLiteOpenHelper, private val gson: Gson) {


    fun clearRoutesDataAsync(): Observable<Unit> = Observable.fromCallable {
        clearRoutesData()
    }.subscribeOn(Schedulers.io())

    private fun clearRoutesData() {
        val database = databaseHelper.writableDatabase
        database.execSQL("DELETE FROM $TABLE_TRIPS")
        database.execSQL("DELETE FROM $TABLE_SEGMENTS")
        database.execSQL("DELETE FROM $TABLE_TRIP_GROUPS")
        database.close()
    }

    open fun deleteAsync(whereClause: Pair<String, Array<String>>): Observable<Int> {
        return Observable
                .fromCallable { delete(whereClause) }
                .subscribeOn(Schedulers.io())
    }

    open fun deleteByTableAsync(tables: List<String>, whereClauses: List<Pair<String, Array<String>>>): Observable<Int> {
        if (tables.size != whereClauses.size) {
            throw IllegalArgumentException("Tables and whereClauses must have the same size")
        }

        return Observable.fromCallable {
            deleteByTablesInTransaction(tables, whereClauses)
        }.subscribeOn(Schedulers.io())
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
                }.onExceptionResumeNext {
                    Observable.just(it)
                }
                .onErrorResumeNext { throwable: Throwable ->
                    if (BuildConfig.DEBUG) {
                        throwable.printStackTrace()
                    }
                    Observable.just(groups)
                }
                .subscribeOn(Schedulers.io())
    }

    open fun queryAsync(query: Pair<String, Array<String>?>): Observable<TripGroup> {
        return queryAsync(query.first, query.second)
    }

    open fun querySegmentByIdAndTripId(segmentId: Long, tripId: String): Single<TripSegment> {
        return Single
                .fromCallable {
                    val database = databaseHelper.readableDatabase
                    val cursor = database.query(TABLE_SEGMENTS,
                            null,
                            "$COL_TRIP_ID = ?",
                            arrayOf(tripId), null, null, null)
                    val tripSegments = asSegments(cursor)
                    cursor.close()
                    tripSegments.first { it.id == segmentId }
                }
                .subscribeOn(Schedulers.io())
    }

    open fun queryTripGroupIdsByRequestIdAsync(requestId: String): Observable<String> {
        return Observable
                .fromCallable {
                    val database = databaseHelper.readableDatabase
                    val cursor = database.query(TABLE_TRIP_GROUPS,
                            arrayOf(COL_UUID),
                            "$COL_REQUEST_ID =?",
                            arrayOf(requestId), null, null, null)
                    var list = mutableListOf<String>()
                    while (cursor.moveToNext()) {
                        list.add(cursor.getString(cursor.getColumnIndex(COL_UUID)))
                    }
                    cursor.close()
                    list
                }.flatMap { Observable.fromIterable(it) }
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
                    values.put(COL_LOG_URL, trip.logURL)
                    values.put(COL_SHARE_URL, trip.shareURL)
                    values.put(COL_PROGRESS_URL, trip.progressURL)
                    values.put(COL_CARBON_COST, trip.carbonCost)
                    values.put(COL_MONEY_COST, trip.moneyCost)
                    values.put(COL_HASSLE_COST, trip.hassleCost)
                    values.put(COL_UUID, trip.uuid())
                    values.put(COL_IS_HIDE_EXACT_TIMES, if (trip.isHideExactTimes) 1 else 0)
                    values.put(COL_QUERY_TIME, trip.queryTime)
                    values.put(COL_QUERY_IS_LEAVE_AFTER, if (trip.queryIsLeaveAfter()) 1 else 0)
                    values.put(COL_SUBSCRIBE_URL, trip.subscribeURL)
                    values.put(COL_UNSUBSCRIBE_URL, trip.unsubscribeURL)
                    values.put(COL_AVAILABILITY, trip.availabilityString)
                    values.put(COL_AVAILABILITY_INFO, trip.availabilityInfo)
                    values.put(COL_MONEY_USD_COST, trip.moneyUsdCost)
                    database.beginTransaction()
                    try {
                        database.update(TABLE_TRIPS, values, "$COL_UUID = ?", arrayOf(oldTripUuid))
                        database.delete(TABLE_SEGMENTS, "$COL_TRIP_ID = ?", arrayOf(oldTripUuid))
                        saveSegments(database, trip.uuid(), trip.segments)
                        database.setTransactionSuccessful()
                    } finally {
                        database.endTransaction()
                    }
                }.subscribeOn(Schedulers.io())
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

    private fun delete(whereClause: Pair<String, Array<String>>): Int {
        val database = databaseHelper.writableDatabase
        return database.delete(TABLE_TRIP_GROUPS, whereClause.first, whereClause.second)
    }

    private fun deleteByTablesInTransaction(tables: List<String>, whereClauses: List<Pair<String, Array<String>>>): Int {
        val database = databaseHelper.writableDatabase
        var totalRowsDeleted = 0

        database.beginTransaction()
        try {
            for (i in tables.indices) {
                val table = tables[i]
                val whereClause = whereClauses[i]
                println("tag123, table: $table")
                println("tag123, where: ${whereClause.first}")
                println("tag123, args: ${whereClause.second.contentToString()}")
                totalRowsDeleted += database.delete(table, whereClause.first, whereClause.second)
            }
            database.setTransactionSuccessful()
        } catch (e: Exception) {
            println("tag123, exception: ${e.message}")
            e.printStackTrace()
        } finally {
            database.endTransaction()
        }

        println("tag123, totalRowsDeleted: $totalRowsDeleted")

        return totalRowsDeleted
    }

    private fun queryAsync(
            selection: String,
            selectionArgs: Array<String>?): Observable<TripGroup> {

        return Observable.using({ databaseHelper.readableDatabase.rawQuery(selection, selectionArgs) },
                { flattenCursor().apply(it) }, { cursor -> cursor.close() })
                .flatMap { groupCursor ->
                    queryTripsOfTripGroup(groupCursor)
                }.subscribeOn(Schedulers.io())
    }

    // groupCursor will be closed by queryAsync()
    private fun queryTripsOfTripGroup(groupCursor: Cursor): Observable<TripGroup> {
        val group = asTripGroup(groupCursor)
        return Observable.using({ databaseHelper.readableDatabase.rawQuery(SELECT_TRIPS, arrayOf(group.uuid())) },
                { flattenCursor().apply(it) },
                { it.close() })
                .flatMap { tripCursor ->
                    querySegmentsOfTrip(tripCursor, groupCursor)
                }
                .toList()
                .map { trips ->
                    group.trips = ArrayList(trips)
                    group
                }.toObservable()
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
        val logUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_LOG_URL))
        val progressUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_PROGRESS_URL))
        val plannedUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_PLANNED_URL))
        val tempUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_TEMP_URL))
        val queryIsLeaveAfter = tripCursor.getInt(tripCursor.getColumnIndex(COL_QUERY_IS_LEAVE_AFTER))
        val mainSegmentHashCode = tripCursor.getLong(tripCursor.getColumnIndex(COL_MAIN_SEGMENT_HASH_CODE))
        val shareUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_SHARE_URL))
        val isHideExactTimes = tripCursor.getInt(tripCursor.getColumnIndex(COL_IS_HIDE_EXACT_TIMES)) == 1
        val queryTime = tripCursor.getLong(tripCursor.getColumnIndex(COL_QUERY_TIME))

        val isNotifable = groupCursor.getInt(groupCursor.getColumnIndex(COL_IS_NOTIFIABLE)) == 1
        val subscribeUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_SUBSCRIBE_URL))
        val unSubscribeUrl = tripCursor.getString(tripCursor.getColumnIndex(COL_UNSUBSCRIBE_URL))
        val availability = tripCursor.getString(tripCursor.getColumnIndex(COL_AVAILABILITY))
        val availabilityInfo = tripCursor.getString(tripCursor.getColumnIndex(COL_AVAILABILITY_INFO))
        val moneyUSDCost = tripCursor.getFloat(tripCursor.getColumnIndex(COL_MONEY_USD_COST))

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
        trip.shareURL = shareUrl
        trip.logURL = logUrl
        trip.progressURL = progressUrl
        trip.plannedURL = plannedUrl
        trip.temporaryURL = tempUrl
        trip.mainSegmentHashCode = mainSegmentHashCode
        trip.isHideExactTimes = isHideExactTimes
        trip.queryTime = queryTime
        trip.setQueryIsLeaveAfter(queryIsLeaveAfter == 1)
        trip.isFavourite(isNotifable)
        trip.subscribeURL = subscribeUrl
        trip.unsubscribeURL = unSubscribeUrl
        trip.setAvailability(availability)
        trip.availabilityInfo = availabilityInfo
        trip.moneyUsdCost = moneyUSDCost
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
        values.put(COL_LOG_URL, trip.logURL)
        values.put(COL_PROGRESS_URL, trip.progressURL)
        values.put(COL_PLANNED_URL, trip.plannedURL)
        values.put(COL_TEMP_URL, trip.temporaryURL)
        values.put(COL_QUERY_IS_LEAVE_AFTER, if (trip.queryIsLeaveAfter()) 1 else 0)
        values.put(COL_MAIN_SEGMENT_HASH_CODE, trip.mainSegmentHashCode)
        values.put(COL_SHARE_URL, trip.shareURL)
        values.put(COL_IS_HIDE_EXACT_TIMES, trip.isHideExactTimes)
        values.put(COL_QUERY_TIME, trip.queryTime)
        values.put(COL_SUBSCRIBE_URL, trip.subscribeURL)
        values.put(COL_UNSUBSCRIBE_URL, trip.unsubscribeURL)
        values.put(COL_AVAILABILITY, trip.availabilityString)
        values.put(COL_AVAILABILITY_INFO, trip.availabilityInfo)
        values.put(COL_MONEY_USD_COST, trip.moneyUsdCost)
        database.insertWithOnConflict(TABLE_TRIPS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

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
