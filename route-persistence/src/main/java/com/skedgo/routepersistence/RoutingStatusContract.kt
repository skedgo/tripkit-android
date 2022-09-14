package com.skedgo.routepersistence

import android.database.sqlite.SQLiteDatabase
import com.skedgo.sqlite.DatabaseField
import com.skedgo.sqlite.DatabaseTable
import com.skedgo.sqlite.UniqueIndices

object RoutingStatusContract {
  const val REQUEST_ID = "requestId"
  const val STATUS = "status"
  const val STATUS_MESSAGE = "statusMessage"
  const val ROUTING_STATUS = "routingStatus"

  fun create(database: SQLiteDatabase) {
    val requestId = DatabaseField(REQUEST_ID, "TEXT")
    val status = DatabaseField(STATUS, "TEXT")
    val message = DatabaseField(STATUS_MESSAGE, "TEXT")
    DatabaseTable(
        ROUTING_STATUS,
        arrayOf(requestId, status, message),
        UniqueIndices.of(ROUTING_STATUS, requestId)
    ).create(database)
  }
}
