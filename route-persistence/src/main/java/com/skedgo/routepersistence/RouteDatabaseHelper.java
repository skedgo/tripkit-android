package com.skedgo.routepersistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDatabaseHelper extends SQLiteOpenHelper {
  public RouteDatabaseHelper(Context context, String name) {
    super(context, name, null, 1);
  }

  @Override public void onCreate(SQLiteDatabase database) {
    RouteContract.createTables(database);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
