package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices


object TripGroupContract {
  const val COL_UUID = "uuid"
  const val COL_GROUP_ID = "groupId"
  const val COL_FREQUENCY = "frequency"
  const val COL_DISPLAY_TRIP_ID = "displayTripId"
  const val COL_ID = "id"
  const val COL_CURRENCY_SYMBOL = "currencySymbol"
  const val COL_SAVE_URL = "saveURL"
  const val COL_UPDATE_URL = "updateURL"
  const val COL_PROGRESS_URL = "progressURL"
  const val COL_PLANNED_URL = "plannedURL"
  const val COL_TEMP_URL = "temporaryURL"
  const val COL_DEPART = "depart"
  const val COL_ARRIVE = "arrive"
  const val COL_QUERY_IS_LEAVE_AFTER = "queryIsLeaveAfter"
  const val COL_CALORIES_COST = "caloriesCost"
  const val COL_MONEY_COST = "moneyCost"
  const val COL_CARBON_COST = "carbonCost"
  const val COL_HASSLE_COST = "hassleCost"
  const val COL_WEIGHTED_SCORE = "weightedScore"
  const val COL_TRIP_ID = "tripId"
  const val COL_JSON = "json"
  const val TABLE_SEGMENTS = "segments"
  const val SELECT_SEGMENTS = "select * from $TABLE_SEGMENTS where tripId = ?"
  const val TABLE_TRIPS = "trips"
  const val SELECT_TRIPS = "select * from $TABLE_TRIPS where $COL_GROUP_ID = ?"
  const val TABLE_TRIP_GROUPS = "tripGroups"
  const val COL_IS_NOTIFIABLE = "isNotifiable"

  fun createTables(database: SQLiteDatabase) {
    val frequency = DatabaseField(COL_FREQUENCY, "integer")
    val displayTripId = DatabaseField(COL_DISPLAY_TRIP_ID, "integer")
    val depart = DatabaseField(COL_DEPART, "integer")
    val arrive = DatabaseField(COL_ARRIVE, "integer")
    val queryIsLeaveAfter = DatabaseField(COL_QUERY_IS_LEAVE_AFTER, "integer")
    val caloriesCost = DatabaseField(COL_CALORIES_COST, "real")
    val tempUrl = DatabaseField(COL_TEMP_URL, "text")
    val plannedUrl = DatabaseField(COL_PLANNED_URL, "text")
    val progressUrl = DatabaseField(COL_PROGRESS_URL, "text")
    val moneyCost = DatabaseField(COL_MONEY_COST, "real")
    val carbonCost = DatabaseField(COL_CARBON_COST, "real")
    val updateUrl = DatabaseField(COL_UPDATE_URL, "text")
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
    val isNotifiable = DatabaseField(COL_IS_NOTIFIABLE, "integer")
    val tripGroups = DatabaseTable(
        TABLE_TRIP_GROUPS,
        arrayOf(_id, uuid, frequency, displayTripId, isNotifiable),
        UniqueIndices.of(TABLE_TRIP_GROUPS, uuid),
        "CREATE TRIGGER deleteTrips AFTER DELETE ON " + TABLE_TRIP_GROUPS + " BEGIN " +
            "DELETE FROM " + TABLE_TRIPS + " WHERE " + groupId + " = old.uuid;" +
            "END;"
    )
    tripGroups.create(database)

    val trips = DatabaseTable(
        TABLE_TRIPS,
        arrayOf(_id, id, groupId, uuid, currencySymbol, saveUrl, depart, arrive, caloriesCost, moneyCost, carbonCost, hassleCost, weightedScore, updateUrl, progressUrl, plannedUrl, tempUrl, queryIsLeaveAfter),
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