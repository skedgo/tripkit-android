package com.skedgo.routepersistence

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.skedgo.routepersistence.RouteContract.createTables
import com.skedgo.routepersistence.RoutingStatusContract.create

class RouteDatabaseHelper(context: Context?, name: String?) :
    SQLiteOpenHelper(context, name, null, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase) {
        createTables(database)
        create(database)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != 1) {
            if (newVersion > 2) {
                try {
                    db.execSQL("ALTER TABLE tripGroups ADD COLUMN sources TEXT")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }

            if (newVersion > 3) {
                try {
                    db.execSQL("ALTER TABLE routingStatus ADD COLUMN statusMessage TEXT")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }

            if (newVersion > 4) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN mainSegmentHashCode INTEGER")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }

            if (newVersion > 5) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN logURL TEXT")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }

            if (newVersion > 6) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN shareURL TEXT")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }
            if (newVersion > 7) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN isHideExactTimes INTEGER")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }
            if (newVersion > 8) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN queryTime LONG")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }
            if (newVersion > 10) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN subscribeURL TEXT")
                    db.execSQL("ALTER TABLE trips ADD COLUMN unsubscribeURL TEXT")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }
            if (newVersion > 11) {
                try {
                    db.execSQL("ALTER TABLE trips ADD COLUMN availability TEXT")
                    db.execSQL("ALTER TABLE trips ADD COLUMN availabilityInfo TEXT")
                    db.execSQL("ALTER TABLE trips ADD COLUMN moneyUSDCost REAL")
                } catch (ex: SQLiteException) {
                    // ignored if the column exists
                }
            }
        } else {
            create(db)
        }
    }

    companion object {
        private const val DATABASE_VERSION = 12
    }
}
