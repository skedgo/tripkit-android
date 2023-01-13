package com.skedgo.tripkit.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import io.reactivex.functions.Consumer;

public final class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private final DatabaseMigrator databaseMigrator;

    public DbHelper(
            @NonNull Context context,
            @NonNull String databaseName,
            @NonNull DatabaseMigrator databaseMigrator) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.databaseMigrator = databaseMigrator;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTables(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (databaseMigrator == null) {
            if (newVersion > 2) {
                try {
                    database.execSQL("ALTER TABLE services ADD COLUMN start_platform TEXT");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }
        } else {
            databaseMigrator.onUpgrade(database, oldVersion, newVersion);
        }
    }

    private void createTables(SQLiteDatabase database) {
        DbTables.LOCATIONS.create(database);
        DbTables.SCHEDULED_STOPS.create(database);
        DbTables.SCHEDULED_STOP_DOWNLOAD_HISTORY.create(database);
        DbTables.SCHEDULED_SERVICES.create(database);
        DbTables.SERVICE_STOPS.create(database);
        DbTables.SEGMENT_SHAPES.create(database);
        DbTables.SERVICE_ALERTS.create(database);
        DbTables.REAL_TIME_UPDATES.create(database);
        DbTables.PRIVATE_VEHICLES.create(database);
//    FavoritesTable.FAVORITES.create(database);
//    ParkingLotContract.PARKING_LOTS.create(database);
//    MyBookingsContract.INSTANCE.createTable(database);
//    EventLocationContract.INSTANCE.create(database);
//    SkedgoifyResponseContract.INSTANCE.create(database);
//    SkedgoifyTripsContract.INSTANCE.create(database);
    }
}