package com.skedgo.tripkit

import android.database.Cursor
import com.google.gson.Gson
import com.skedgo.tripkit.common.model.TransportMode
import io.reactivex.functions.Function

internal class CursorToTransportModeConverter : Function<Cursor, TransportMode> {
    private val gson = Gson()

    override fun apply(cursor: Cursor): TransportMode {
        val json = cursor.getString(cursor.getColumnIndex(Tables.FIELD_JSON.name))
        return gson.fromJson(json, TransportMode::class.java)
    }
}