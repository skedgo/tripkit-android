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
    DatabaseTable(RETURN_TRIPS,
        arrayOf(tripGroupId, manualTripId),
        UniqueIndices.of(RETURN_TRIPS, tripGroupId),
        UniqueIndices.of(RETURN_TRIPS, manualTripId)
    ).create(database)
  }
}