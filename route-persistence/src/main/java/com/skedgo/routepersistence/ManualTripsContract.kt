package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import com.skedgo.routepersistence.RouteContract.*
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices

object ManualTripsContract {

  const val TRIPGROUP_ID = "tripGroupId"
  const val DURATION_STAYING_AT_END_IN_SECONDS = "durationAtEnd"
  const val MANUAL_TRIPS = "ManualTrips"

  fun create(database: SQLiteDatabase) {
    val tripGroupId = DatabaseField(TRIPGROUP_ID, "TEXT", "NON NULL")
    val stayingAtEnd = DatabaseField(DURATION_STAYING_AT_END_IN_SECONDS, "INTEGER", "NON NULL")
    DatabaseTable(MANUAL_TRIPS,
        arrayOf(tripGroupId, stayingAtEnd),
        UniqueIndices.of(MANUAL_TRIPS, tripGroupId)
    ).create(database)
  }
}