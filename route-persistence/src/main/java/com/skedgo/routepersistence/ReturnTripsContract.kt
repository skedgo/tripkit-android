package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import com.skedgo.routepersistence.ManualTripsContract.TABLE_MANUAL_TRIPS
import com.skedgo.routepersistence.RouteContract.COL_UUID
import com.skedgo.routepersistence.RouteContract.TABLE_TRIP_GROUPS
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.UniqueIndices

object ReturnTripsContract {

  const val TRIPGROUP_ID = "tripGroupId"
  const val MANUAL_TRIP_ID = "manualTripId"
  const val RETURN_TRIPS = "ReturnTrips"

  fun create(database: SQLiteDatabase) {
    val tripGroupId = DatabaseField(TRIPGROUP_ID, "TEXT", "NOT NULL")
    val manualTripId = DatabaseField(MANUAL_TRIP_ID, "TEXT", "NOT NULL")

    database.execSQL("CREATE TABLE $RETURN_TRIPS(" +
        "$TRIPGROUP_ID TEXT NOT NULL, " +
        "$MANUAL_TRIP_ID TEXT NOT NULL, " +
        "FOREIGN KEY($TRIPGROUP_ID) REFERENCES $TABLE_TRIP_GROUPS($COL_UUID), " +
        "FOREIGN KEY($MANUAL_TRIP_ID) REFERENCES $TABLE_MANUAL_TRIPS(${ManualTripsContract.TRIPGROUP_ID}))")
    database.execSQL(UniqueIndices.of(RETURN_TRIPS, tripGroupId))
    database.execSQL(UniqueIndices.of(RETURN_TRIPS, manualTripId))
  }
}