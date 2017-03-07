package com.skedgo.routepersistence

import android.content.ContentValues
import android.database.Cursor
import com.google.gson.Gson
import com.skedgo.android.common.model.TripSegment
import com.skedgo.routepersistence.TripGroupContract.COL_JSON
import skedgo.sqlite.SQLiteEntityAdapter


class TripSegmentEntityAdapter constructor(val gson: Gson): SQLiteEntityAdapter<TripSegment> {

  override fun toContentValues(segment: TripSegment): ContentValues {
    val values = ContentValues()
    values.put(COL_JSON, gson.toJson(segment, TripSegment::class.java))
    return values
  }

  override fun toEntity(cursor: Cursor): TripSegment {
    val json = cursor.getString(cursor.getColumnIndex(COL_JSON))
    return gson.fromJson(json, TripSegment::class.java)
  }
}