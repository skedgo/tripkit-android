package com.skedgo.android.tripkit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;
import skedgo.sqlite.Cursors;

final class RegionDatabaseHelper extends SQLiteOpenHelper {
  RegionDatabaseHelper(Context context, String name) {
    super(context, name, null, 2);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Tables.REGIONS.getCreateSql());
    db.execSQL(Tables.TRANSPORT_MODES.getCreateSql());
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.delete(Tables.REGIONS.getName(), null, null);
    db.delete(Tables.TRANSPORT_MODES.getName(), null, null);
  }

  public Observable<List<Region>> loadRegionsAsync() {
    return Observable
        .create(new OnSubscribeLoadRegions(this))
        .flatMap(Cursors.flattenCursor())
        .map(new CursorToRegionConverter())
        .toList()
        .takeFirst(Utils.<Region>isNotEmpty())
        .subscribeOn(Schedulers.io());
  }

  public Observable<Map<String, TransportMode>> loadModesAsync() {
    return Observable
        .create(new OnSubscribeLoadTransportModes(this))
        .flatMap(Cursors.flattenCursor())
        .map(new CursorToTransportModeConverter())
        .toList()
        .takeFirst(Utils.<TransportMode>isNotEmpty())
        .map(Utils.toModeMap())
        .subscribeOn(Schedulers.io());
  }
}