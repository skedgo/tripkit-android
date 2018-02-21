package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import com.skedgo.routepersistence.RouteContract.*
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices

object ManualTripsContract {

  const val TRIPGROUP_ID = "tripGroupId"
  const val DURATION_STAYING_AT_END_IN_SECONDS = "durationAtEnd"
  const val TABLE_MANUAL_TRIPS = "ManualTrips"

  fun create(database: SQLiteDatabase) {
    val tripGroupId = DatabaseField(TRIPGROUP_ID, "TEXT", "NON NULL")
    database.execSQL("CREATE TABLE $TABLE_MANUAL_TRIPS(" +
        "$TRIPGROUP_ID TEXT NON NULL," +
        "$DURATION_STAYING_AT_END_IN_SECONDS INTEGER," +
        "FOREIGN KEY($TRIPGROUP_ID) REFERENCES $TABLE_TRIP_GROUPS($COL_UUID))")
    database.execSQL(UniqueIndices.of(TABLE_MANUAL_TRIPS, tripGroupId))
  }
}