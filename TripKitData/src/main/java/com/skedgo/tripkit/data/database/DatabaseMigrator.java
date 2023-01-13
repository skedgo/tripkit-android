package com.skedgo.tripkit.data.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import io.reactivex.functions.Consumer;

public class DatabaseMigrator {
    private TripKitDatabase database;

    public DatabaseMigrator(
            TripKitDatabase database
    ) {
        this.database = database;
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (newVersion > 2) {
            try {
                database.execSQL("ALTER TABLE services ADD COLUMN start_platform TEXT");
            } catch (SQLiteException ex) {
                // ignored if the column exists
            }
        }
    }
}