package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import com.skedgo.routepersistence.RouteContract.*
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices

object ManualTripsContract {

  const val TRIPGROUP_ID = "requestId"
  const val DURATION_STAYING_AT_START_IN_SECONDS = "durationAtStart"
  const val DURATION_STAYING_AT_END_IN_SECONDS = "durationAtEnd"
  const val MANUAL_TRIPS = "ManualTripsContract"

  fun create(database: SQLiteDatabase) {
    val tripGroupId = DatabaseField(TRIPGROUP_ID, "TEXT", "PRIMARY KEY")
//    val durationAtStart = DatabaseField(DURATION_STAYING_AT_START_IN_SECONDS, "INTEGER")
//    val durationAtEnd = DatabaseField(DURATION_STAYING_AT_END_IN_SECONDS, "INTEGER")
//    DatabaseTable(
//        MANUAL_TRIPS,
//        arrayOf(tripGroupId, durationAtStart, durationAtEnd),
//        "FOREIGN KEY($TRIPGROUP_ID) REFERENCES $TABLE_TRIP_GROUPS($COL_UUID)",
//        UniqueIndices.of(MANUAL_TRIPS, tripGroupId)
//    ).create(database)
    database.execSQL("CREATE TABLE $MANUAL_TRIPS(" +
        "$TRIPGROUP_ID TEXT PRIMARY KEY," +
        "$DURATION_STAYING_AT_START_IN_SECONDS INTEGER," +
        "$DURATION_STAYING_AT_END_IN_SECONDS INTEGER," +
        "FOREIGN KEY($TRIPGROUP_ID) REFERENCES $TABLE_TRIP_GROUPS($COL_UUID))")
    database.execSQL(UniqueIndices.of(MANUAL_TRIPS, tripGroupId))
  }
}