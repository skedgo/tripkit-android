package com.skedgo.routepersistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class RouteDatabaseHelper extends SQLiteOpenHelper {
  private static int DATABASE_VERSION = 3;

  public RouteDatabaseHelper(Context context, String name) {
    super(context, name, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase database) {
    RouteContract.createTables(database);
    RoutingStatusContract.INSTANCE.create(database);
    ManualTripsContract.INSTANCE.create(database);
    ReturnTripsContract.INSTANCE.create(database);
  }

  @Override public void onConfigure(SQLiteDatabase db) {
    super.onConfigure(db);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      db.setForeignKeyConstraintsEnabled(true);
    } else {
      db.execSQL("PRAGMA foreign_keys=true");
    }
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    switch (oldVersion) {
      case 1:
        RoutingStatusContract.INSTANCE.create(db);

      case 2:
        ManualTripsContract.INSTANCE.create(db);
        ReturnTripsContract.INSTANCE.create(db);
    }
  }
}
