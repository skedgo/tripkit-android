package com.skedgo.android.tripkit;

import android.database.Cursor;

import com.google.gson.Gson;
import com.skedgo.android.common.model.Region;

import rx.functions.Func1;

final class CursorToRegionConverter implements Func1<Cursor, Region> {
  private final Gson gson = new Gson();

  @Override
  public Region call(Cursor cursor) {
    final String json = cursor.getString(cursor.getColumnIndex(Tables.FIELD_JSON.getName()));
    return gson.fromJson(json, Region.class);
  }
}