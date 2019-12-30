package com.skedgo.tripkit;

import android.database.Cursor;

import com.google.gson.Gson;
import com.skedgo.tripkit.common.model.Region;
import io.reactivex.functions.Function;

final class CursorToRegionConverter implements Function<Cursor, Region> {
  private final Gson gson = new Gson();

  @Override
  public Region apply(Cursor cursor) {
    final String json = cursor.getString(cursor.getColumnIndex(Tables.FIELD_JSON.getName()));
    return gson.fromJson(json, Region.class);
  }
}