package com.skedgo.tripkit

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.skedgo.sqlite.Cursors.flattenCursor
import com.skedgo.tripkit.common.model.TransportMode
import com.skedgo.tripkit.common.model.region.Region
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class RegionDatabaseHelper(context: Context?, name: String?) :
    SQLiteOpenHelper(context, name, null, 2) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Tables.REGIONS.getCreateSql())
        db.execSQL(Tables.TRANSPORT_MODES.getCreateSql())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.delete(Tables.REGIONS.name, null, null)
        db.delete(Tables.TRANSPORT_MODES.name, null, null)
    }

    fun loadRegionsAsync(): Observable<List<Region>> {
        return Observable
            .create(OnSubscribeLoadRegions(this))
            .flatMap(flattenCursor())
            .map(CursorToRegionConverter())
            .toList()
            .filter(Utils.isNotEmpty()).toObservable().firstOrError().toObservable()
            .subscribeOn(Schedulers.io())
    }

    fun loadModesAsync(): Observable<Map<String, TransportMode>> {
        return Observable
            .create(OnSubscribeLoadTransportModes(this))
            .flatMap(flattenCursor())
            .map(CursorToTransportModeConverter())
            .toList()
            .filter(Utils.isNotEmpty()).toObservable()
            .map(Utils.toModeMap())
            .subscribeOn(Schedulers.io())
    }
}