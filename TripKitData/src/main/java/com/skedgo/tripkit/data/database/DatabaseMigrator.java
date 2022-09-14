package com.skedgo.tripkit.data.database;

import android.database.sqlite.SQLiteDatabase;
import io.reactivex.functions.Consumer;

public class DatabaseMigrator {
  private TripKitDatabase database;

  public DatabaseMigrator(
          TripKitDatabase database
  ) {
    this.database = database;
  }

  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
  }
}