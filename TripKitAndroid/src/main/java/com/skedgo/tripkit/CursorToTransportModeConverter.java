package com.skedgo.tripkit;

import android.database.Cursor;

import com.google.gson.Gson;
import com.skedgo.android.common.model.TransportMode;
import io.reactivex.functions.Function;

final class CursorToTransportModeConverter implements Function<Cursor, TransportMode> {
  private final Gson gson = new Gson();

  @Override
  public TransportMode apply(Cursor cursor) {
    final String json = cursor.getString(cursor.getColumnIndex(Tables.FIELD_JSON.getName()));
    return gson.fromJson(json, TransportMode.class);
  }
}