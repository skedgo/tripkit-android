package com.skedgo.routepersistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class RouteDatabaseHelper extends SQLiteOpenHelper {
  private static int DATABASE_VERSION = 5;

  public RouteDatabaseHelper(Context context, String name) {
    super(context, name, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase database) {
    RouteContract.createTables(database);
    RoutingStatusContract.INSTANCE.create(database);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    switch (oldVersion) {
      case 1:
        RoutingStatusContract.INSTANCE.create(db);
      case 2:
        db.execSQL("ALTER TABLE tripGroups ADD COLUMN sources TEXT");
      case 3:
        try {
          db.execSQL("ALTER TABLE routingStatus ADD COLUMN statusMessage TEXT");
        } catch (SQLiteException ex) {
          // ignored if the column exists
        }
      case 4:
        try {
          db.execSQL("ALTER TABLE trips ADD COLUMN mainSegmentHashCode INTEGER");
        } catch (SQLiteException ex) {
        }
    }
  }
}
