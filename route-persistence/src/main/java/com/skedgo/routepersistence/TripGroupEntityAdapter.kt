package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.Cursor
import com.skedgo.android.common.model.TripGroup
import com.skedgo.routepersistence.TripGroupContract.COL_DISPLAY_TRIP_ID
import com.skedgo.routepersistence.TripGroupContract.COL_FREQUENCY
import com.skedgo.routepersistence.TripGroupContract.COL_IS_NOTIFIABLE
import com.skedgo.routepersistence.TripGroupContract.COL_UUID
import skedgo.sqlite.SQLiteEntityAdapter
import javax.inject.Inject


class TripGroupEntityAdapter constructor() {

  fun toEntity(cursor: Cursor): TripGroup {
    val uuid = cursor.getString(cursor.getColumnIndex(COL_UUID))
    val frequency = cursor.getInt(cursor.getColumnIndex(COL_FREQUENCY))
    val displayTripId = cursor.getLong(cursor.getColumnIndex(COL_DISPLAY_TRIP_ID))

    val group = TripGroup()
    group.uuid(uuid)
    group.frequency = frequency
    group.displayTripId = displayTripId
    return group
  }

  fun toContentValues(group: TripGroup, isNotifiable: Boolean): ContentValues {
    val values = ContentValues()
    values.put(COL_UUID, group.uuid())
    values.put(COL_FREQUENCY, group.frequency)
    values.put(COL_DISPLAY_TRIP_ID, group.displayTripId)
    values.put(COL_IS_NOTIFIABLE, if (isNotifiable) 1 else 0)

    return values;
  }
}