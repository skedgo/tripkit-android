package com.skedgo.routepersistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.skedgo.android.common.model.TripGroup;

public class RouteDatabaseHelper extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 2;

  public RouteDatabaseHelper(Context context, String name) {
    super(context, name, null, DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase database) {
    TripGroupContract.INSTANCE.createTables(database);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    switch (oldVersion) {
      case 1:
        db.execSQL("DROP TABLE IF EXISTS " + TripGroupContract.TABLE_TRIP_GROUPS );
        db.execSQL("DROP TABLE IF EXISTS " + TripGroupContract.TABLE_TRIPS );
        db.execSQL("DROP TABLE IF EXISTS " + TripGroupContract.TABLE_SEGMENTS );
        TripGroupContract.INSTANCE.createTables(db);
        RouteContract.INSTANCE.createTable(db);
    }
  }
}
