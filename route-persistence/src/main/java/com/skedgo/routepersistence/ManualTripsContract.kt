package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices

object ManualTripsContract {

  const val TRIPGROUP_ID = "requestId"
  const val DURATION_STAYING_AT_START = "durationAtStart"
  const val DURATION_STAYING_AT_END = "durationAtEnd"
  const val START_TIME = "startTime"
  const val END_TIME = "endTime"
  const val MANUAL_TRIPS = "ManualTripsContract"

  fun create(database: SQLiteDatabase) {
    val tripGroupId = DatabaseField(TRIPGROUP_ID, "TEXT")
    val durationAtStart = DatabaseField(DURATION_STAYING_AT_START, "INTEGER")
    val durationAtEnd = DatabaseField(DURATION_STAYING_AT_END, "INTEGER")
    val startTime = DatabaseField(DURATION_STAYING_AT_END, "INTEGER")
    val endTime = DatabaseField(DURATION_STAYING_AT_END, "INTEGER")
    DatabaseTable(
        MANUAL_TRIPS,
        arrayOf(tripGroupId, startTime, endTime, durationAtStart, durationAtEnd),
        UniqueIndices.of(MANUAL_TRIPS, tripGroupId)
    ).create(database)
  }
}