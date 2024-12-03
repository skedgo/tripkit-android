package com.skedgo.tripkit

import android.database.Cursor
import com.google.gson.Gson
import com.skedgo.tripkit.common.model.region.Region
import io.reactivex.functions.Function

internal class CursorToRegionConverter : Function<Cursor, Region> {
    private val gson = Gson()

    override fun apply(cursor: Cursor): Region {
        val json = cursor.getString(cursor.getColumnIndex(Tables.FIELD_JSON.name))
        return gson.fromJson(json, Region::class.java)
    }
}