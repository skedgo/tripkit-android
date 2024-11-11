package com.skedgo.tripkit

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.common.model.region.RegionsResponse
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

class OnSubscribeSaveRegionsResponse(
    private val database: SQLiteDatabase,
    private val response: RegionsResponse
) : ObservableOnSubscribe<Void> {
    private val gson = Gson()

    @Throws(Exception::class)
    override fun subscribe(emitter: ObservableEmitter<Void>) {
        try {
            database.beginTransaction()

            database.delete(Tables.REGIONS.name, null, null)
            database.delete(Tables.TRANSPORT_MODES.name, null, null)

            val regions = response.regions
            if (regions != null) {
                for (region in regions) {
                    database.insert(
                        Tables.REGIONS.name,
                        null,
                        toRegionValues(region)
                    )
                }
            }

            val modes = response.transportModes
            if (modes != null) {
                for (mode in modes) {
                    database.insert(
                        Tables.TRANSPORT_MODES.name,
                        null,
                        toTransportModeValues(mode)
                    )
                }
            }

            database.setTransactionSuccessful()
            emitter.onComplete()
        } catch (e: Exception) {
            emitter.onError(e)
        } finally {
            database.endTransaction()
        }
    }

    fun toRegionValues(region: Region): ContentValues {
        val values = ContentValues(1)
        values.put(Tables.FIELD_JSON.name, gson.toJson(region))
        return values
    }

    fun toTransportModeValues(mode: TransportMode): ContentValues {
        val values = ContentValues(1)
        values.put(Tables.FIELD_JSON.name, gson.toJson(mode))
        return values
    }
}