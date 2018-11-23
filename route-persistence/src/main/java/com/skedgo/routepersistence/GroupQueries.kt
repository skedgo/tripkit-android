package com.skedgo.routepersistence

import android.util.Pair

import com.skedgo.android.common.model.Query
import skedgo.tripkit.routing.TripGroup

import java.util.UUID

import com.skedgo.routepersistence.RouteContract.COL_IS_NOTIFIABLE
import com.skedgo.routepersistence.RouteContract.COL_REQUEST_ID
import com.skedgo.routepersistence.RouteContract.COL_UUID
import com.skedgo.routepersistence.RouteContract.TABLE_TRIP_GROUPS

object GroupQueries {

  val isNotifiable: Pair<String, Array<String>?>
    get() = Pair.create(
        "select * from " + TABLE_TRIP_GROUPS
            + " where " + COL_IS_NOTIFIABLE + " = 1",
        null
    )


  /**
   * @param id Should be [Query.uuid] or
   * whatever [UUID.toString] that
   * [TripGroup]s were associated with when saving them.
   */
  fun hasRequestId(id: String): Pair<String, Array<String>?> {
    return Pair.create(
        "select * from " + TABLE_TRIP_GROUPS
            + " where " + COL_REQUEST_ID + " = ?",
        arrayOf(id)
    )
  }

  /**
   * @param uuid Should be [TripGroup.uuid].
   */
  fun hasUuid(uuid: String): Pair<String, Array<String>?> {
    return Pair.create(
        "select * from " + TABLE_TRIP_GROUPS
            + " where " + COL_UUID + " = ?",
        arrayOf(uuid)
    )
  }
}
