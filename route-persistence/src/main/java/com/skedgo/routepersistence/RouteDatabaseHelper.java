package com.skedgo.routepersistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class RouteDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 11;

    public RouteDatabaseHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        RouteContract.createTables(database);
        RoutingStatusContract.INSTANCE.create(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != 1) {
            if (newVersion > 2) {
                try {
                    db.execSQL("ALTER TABLE tripGroups ADD COLUMN sources TEXT");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }

            if (newVersion > 3) {
                try {
                    db.execSQL("ALTER TABLE routingStatus ADD COLUMN statusMessage TEXT");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }

            if (newVersion > 4) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN mainSegmentHashCode INTEGER");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }

            if (newVersion > 5) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN logURL TEXT");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }

            if (newVersion > 6) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN shareURL TEXT");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }
            if (newVersion > 7) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN isHideExactTimes INTEGER");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }
            if (newVersion > 8) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN queryTime LONG");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }
            if(newVersion > 10) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN subscribeURL TEXT");
                } catch (SQLiteException ex) {
                    // ignored if the column exists
                }
            }
        } else {
            RoutingStatusContract.INSTANCE.create(db);
        }
    }
}
