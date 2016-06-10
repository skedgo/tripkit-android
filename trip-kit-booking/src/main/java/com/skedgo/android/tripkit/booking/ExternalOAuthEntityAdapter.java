package com.skedgo.android.tripkit.booking;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import skedgo.sqlite.SQLiteEntityAdapter;

public class ExternalOAuthEntityAdapter implements SQLiteEntityAdapter<ExternalOAuth> {

  @NonNull @Override public ExternalOAuth toEntity(@NonNull Cursor cursor) {
    String authServiceId = cursor.getString(cursor.getColumnIndex(ExternalOAuthTable.AUTH_SERVICE_ID.getName()));
    String token = cursor.getString(cursor.getColumnIndex(ExternalOAuthTable.TOKEN.getName()));
    String refreshToken = cursor.getString(cursor.getColumnIndex(ExternalOAuthTable.REFRESH_TOKEN.getName()));
    int expiresIn = cursor.getInt(cursor.getColumnIndex(ExternalOAuthTable.EXPIRES_IN.getName()));

    return ExternalOAuth.builder()
        .authServiceId(authServiceId)
        .token(token)
        .refreshToken(refreshToken)
        .expiresIn(expiresIn)
        .build();
  }

  @NonNull @Override public ContentValues toContentValues(@NonNull ExternalOAuth entity) {
    final ContentValues values = new ContentValues(ExternalOAuthTable.EXTERNAL_AUTHS.getFields().length);
    values.put(ExternalOAuthTable.AUTH_SERVICE_ID.getName(), entity.authServiceId());
    values.put(ExternalOAuthTable.TOKEN.getName(), entity.token());
    values.put(ExternalOAuthTable.REFRESH_TOKEN.getName(), entity.refreshToken());
    values.put(ExternalOAuthTable.EXPIRES_IN.getName(), entity.expiresIn());

    return values;
  }
}