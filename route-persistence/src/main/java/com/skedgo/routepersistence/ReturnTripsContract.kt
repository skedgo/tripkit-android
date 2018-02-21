package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices

object ReturnTripsContract {

  const val TRIPGROUP_ID = "tripGroupId"
  const val MANUAL_TRIP_ID = "manualTripId"
  const val RETURN_TRIPS = "ReturnTrips"

  fun create(database: SQLiteDatabase) {
    val tripGroupId = DatabaseField(TRIPGROUP_ID, "TEXT", "NON NULL")
    val manualTripId = DatabaseField(MANUAL_TRIP_ID, "TEXT", "NON NULL")

    database.execSQL("CREATE TABLE ${RETURN_TRIPS}(" +
        "${TRIPGROUP_ID} TEXT NON NULL," +
        "${MANUAL_TRIP_ID} TEXT NON NULL," +
        "FOREIGN KEY($TRIPGROUP_ID) REFERENCES ${RouteContract.TABLE_TRIP_GROUPS}(${RouteContract.COL_UUID}), " +
        "FOREIGN KEY($MANUAL_TRIP_ID) REFERENCES ${ManualTripsContract.TABLE_MANUAL_TRIPS}(${ManualTripsContract.TRIPGROUP_ID}))")
    database.execSQL(UniqueIndices.of(RETURN_TRIPS, tripGroupId))
    database.execSQL(UniqueIndices.of(RETURN_TRIPS, manualTripId))
  }
}