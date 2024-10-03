package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import com.skedgo.sqlite.DatabaseField
import com.skedgo.sqlite.DatabaseTable
import com.skedgo.sqlite.UniqueIndices

internal object RouteContract {
    const val COL_REQUEST_ID: String = "requestId"
    const val COL_UUID: String = "uuid"
    const val COL_GROUP_ID: String = "groupId"
    const val COL_FREQUENCY: String = "frequency"
    const val COL_DISPLAY_TRIP_ID: String = "displayTripId"
    const val COL_ID: String = "id"
    const val COL_CURRENCY_SYMBOL: String = "currencySymbol"
    const val COL_SAVE_URL: String = "saveURL"
    const val COL_UPDATE_URL: String = "updateURL"
    const val COL_LOG_URL: String = "logURL"
    const val COL_SHARE_URL: String = "shareURL"
    const val COL_PROGRESS_URL: String = "progressURL"
    const val COL_PLANNED_URL: String = "plannedURL"
    const val COL_TEMP_URL: String = "temporaryURL"
    const val COL_DEPART: String = "depart"
    const val COL_ARRIVE: String = "arrive"
    const val COL_QUERY_IS_LEAVE_AFTER: String = "queryIsLeaveAfter"
    const val COL_CALORIES_COST: String = "caloriesCost"
    const val COL_MAIN_SEGMENT_HASH_CODE: String = "mainSegmentHashCode"
    const val COL_MONEY_COST: String = "moneyCost"
    const val COL_CARBON_COST: String = "carbonCost"
    const val COL_HASSLE_COST: String = "hassleCost"
    const val COL_WEIGHTED_SCORE: String = "weightedScore"
    const val COL_TRIP_ID: String = "tripId"
    const val COL_JSON: String = "json"
    const val TABLE_SEGMENTS: String = "segments"
    const val SELECT_SEGMENTS: String = "select * from " + TABLE_SEGMENTS + " where tripId = ?"
    const val TABLE_TRIPS: String = "trips"
    const val SELECT_TRIPS: String =
        "select * from " + TABLE_TRIPS + " where " + COL_GROUP_ID + " = ?"
    const val TABLE_TRIP_GROUPS: String = "tripGroups"
    const val COL_IS_NOTIFIABLE: String = "isNotifiable"
    const val COL_IS_HIDE_EXACT_TIMES: String = "isHideExactTimes"
    const val COL_QUERY_TIME: String = "queryTime"
    const val COL_SOURCES: String = "sources"
    const val COL_SUBSCRIBE_URL: String = "subscribeURL"
    const val COL_UNSUBSCRIBE_URL: String = "unsubscribeURL"
    const val COL_AVAILABILITY: String = "availability"
    const val COL_AVAILABILITY_INFO: String = "availabilityInfo"
    const val COL_MONEY_USD_COST: String = "moneyUSDCost"

    @JvmStatic
    fun createTables(database: SQLiteDatabase?) {
        val frequency = DatabaseField(COL_FREQUENCY, "integer")
        val displayTripId = DatabaseField(COL_DISPLAY_TRIP_ID, "integer")
        val depart = DatabaseField(COL_DEPART, "integer")
        val arrive = DatabaseField(COL_ARRIVE, "integer")
        val queryIsLeaveAfter = DatabaseField(COL_QUERY_IS_LEAVE_AFTER, "integer")
        val caloriesCost = DatabaseField(COL_CALORIES_COST, "real")
        val mainSegmentHashCode = DatabaseField(COL_MAIN_SEGMENT_HASH_CODE, "integer")
        val tempUrl = DatabaseField(COL_TEMP_URL, "text")
        val plannedUrl = DatabaseField(COL_PLANNED_URL, "text")
        val progressUrl = DatabaseField(COL_PROGRESS_URL, "text")
        val moneyCost = DatabaseField(COL_MONEY_COST, "real")
        val carbonCost = DatabaseField(COL_CARBON_COST, "real")
        val updateUrl = DatabaseField(COL_UPDATE_URL, "text")
        val logUrl = DatabaseField(COL_LOG_URL, "text")
        val shareUrl = DatabaseField(COL_SHARE_URL, "text")
        val saveUrl = DatabaseField(COL_SAVE_URL, "text")
        val currencySymbol = DatabaseField(COL_CURRENCY_SYMBOL, "text")
        val hassleCost = DatabaseField(COL_HASSLE_COST, "real")
        val weightedScore = DatabaseField(COL_WEIGHTED_SCORE, "real")
        val tripId = DatabaseField(COL_TRIP_ID, "text")
        val _id = DatabaseField("_id", "integer", "primary key autoincrement")
        val id = DatabaseField(COL_ID, "integer")
        val groupId = DatabaseField(COL_GROUP_ID, "text")
        val json = DatabaseField(COL_JSON, "text")
        val uuid = DatabaseField(COL_UUID, "text")
        val requestId = DatabaseField(COL_REQUEST_ID, "text")
        val isNotifiable = DatabaseField(COL_IS_NOTIFIABLE, "integer")
        val isHideExactTimes = DatabaseField(COL_IS_HIDE_EXACT_TIMES, "integer")
        val queryTime = DatabaseField(COL_QUERY_TIME, "integer")
        val sources = DatabaseField(COL_SOURCES, "text")
        val subscribeUrl = DatabaseField(COL_SUBSCRIBE_URL, "text")
        val unSubscribeUrl = DatabaseField(COL_UNSUBSCRIBE_URL, "text")
        val availability = DatabaseField(COL_AVAILABILITY, "text")
        val availabilityInfo = DatabaseField(COL_AVAILABILITY_INFO, "text")
        val moneyUsdCost = DatabaseField(COL_MONEY_USD_COST, "real")
        val tripGroups = DatabaseTable(
            TABLE_TRIP_GROUPS,
            arrayOf(
                _id, requestId, uuid, frequency, displayTripId, isNotifiable, sources
            ),
            UniqueIndices.of(TABLE_TRIP_GROUPS, requestId, uuid),
            "CREATE TRIGGER deleteTrips AFTER DELETE ON " + TABLE_TRIP_GROUPS + " BEGIN " +
                "DELETE FROM " + TABLE_TRIPS + " WHERE " + groupId + " = old.uuid;" +
                "END;"
        )
        tripGroups.create(database!!)

        val trips = DatabaseTable(
            TABLE_TRIPS,
            arrayOf(
                _id,
                id,
                groupId,
                uuid,
                currencySymbol,
                saveUrl,
                depart,
                arrive,
                caloriesCost,
                moneyCost,
                carbonCost,
                hassleCost,
                weightedScore,
                updateUrl,
                progressUrl,
                plannedUrl,
                tempUrl,
                queryIsLeaveAfter,
                logUrl,
                shareUrl,
                mainSegmentHashCode,
                isHideExactTimes,
                queryTime,
                subscribeUrl,
                unSubscribeUrl,
                availability,
                availabilityInfo,
                moneyUsdCost
            ),
            UniqueIndices.of(TABLE_TRIPS, id, groupId, uuid),
            "CREATE TRIGGER deleteSegments AFTER DELETE ON " + TABLE_TRIPS + " BEGIN " +
                "DELETE FROM " + TABLE_SEGMENTS + " WHERE " + tripId + " = old.uuid;" +
                "END;"
        )
        trips.create(database)

        val segments = DatabaseTable(
            TABLE_SEGMENTS,
            arrayOf(_id, tripId, json)
        )
        segments.create(database)
    }
}
