package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import com.skedgo.routepersistence.RouteContract.*
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices

object ManualTripsContract {

  const val TRIPGROUP_ID = "tripGroupId"
  const val MANUAL_TRIP_ID = "manualTripId"
  const val DURATION_STAYING_AT_END_IN_SECONDS = "durationAtEnd"
  const val MANUAL_TRIPS = "ManualTripsContract"

  fun create(database: SQLiteDatabase) {
    val tripGroupId = DatabaseField(TRIPGROUP_ID, "TEXT", "NON NULL")
    database.execSQL("CREATE TABLE $MANUAL_TRIPS(" +
        "$TRIPGROUP_ID TEXT NON NULL," +
        "$MANUAL_TRIP_ID TEXT NON NULL PRIMARY KEY," +
        "$DURATION_STAYING_AT_END_IN_SECONDS INTEGER," +
        "FOREIGN KEY($TRIPGROUP_ID) REFERENCES $TABLE_TRIP_GROUPS($COL_UUID))")
    database.execSQL(UniqueIndices.of(MANUAL_TRIPS, tripGroupId))
  }
}