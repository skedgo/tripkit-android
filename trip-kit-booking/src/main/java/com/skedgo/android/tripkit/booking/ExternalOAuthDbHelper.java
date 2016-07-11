package com.skedgo.android.tripkit.booking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class ExternalOAuthDbHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;

  public ExternalOAuthDbHelper(@NonNull Context context, @NonNull String databaseName) {
    super(context, databaseName, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    ExternalOAuthTable.EXTERNAL_AUTHS.create(db);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}