package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase

import skedgo.sqlite.DatabaseField
import skedgo.sqlite.DatabaseTable
import skedgo.sqlite.UniqueIndices

object RouteContract {
  const val ROUTES = "routes"

  const val TRIP_GROUP_ID = "tripgroup_id"
  const val ROUTE_ID = "route_id"

  fun createTable(database: SQLiteDatabase) {
    val routeId = DatabaseField("route_id", "TEXT", "COLLATE NOCASE")
    val tripGroupId = DatabaseField("tripgroup_id", "TEXT", "COLLATE NOCASE")
    val ROUTE = DatabaseTable(
        ROUTES,
        arrayOf(tripGroupId, routeId),
        UniqueIndices.of(ROUTES, tripGroupId, routeId)
    )
    ROUTE.create(database)
  }
}
