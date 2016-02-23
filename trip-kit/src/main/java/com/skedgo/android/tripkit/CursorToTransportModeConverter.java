package com.skedgo.android.tripkit;

import android.database.Cursor;

import com.google.gson.Gson;
import com.skedgo.android.common.model.TransportMode;

import rx.functions.Func1;

final class CursorToTransportModeConverter implements Func1<Cursor, TransportMode> {
  private final Gson gson = new Gson();

  @Override
  public TransportMode call(Cursor cursor) {
    final String json = cursor.getString(cursor.getColumnIndex(Tables.FIELD_JSON.getName()));
    return gson.fromJson(json, TransportMode.class);
  }
}