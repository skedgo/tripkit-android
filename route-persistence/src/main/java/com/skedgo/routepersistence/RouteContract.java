package com.skedgo.routepersistence;

import android.database.sqlite.SQLiteDatabase;

import com.skedgo.sqlite.DatabaseField;
import com.skedgo.sqlite.DatabaseTable;
import com.skedgo.sqlite.UniqueIndices;

final class RouteContract {
    static final String COL_REQUEST_ID = "requestId";
    static final String COL_UUID = "uuid";
    static final String COL_GROUP_ID = "groupId";
    static final String COL_FREQUENCY = "frequency";
    static final String COL_DISPLAY_TRIP_ID = "displayTripId";
    static final String COL_ID = "id";
    static final String COL_CURRENCY_SYMBOL = "currencySymbol";
    static final String COL_SAVE_URL = "saveURL";
    static final String COL_UPDATE_URL = "updateURL";
    static final String COL_LOG_URL = "logURL";
    static final String COL_SHARE_URL = "shareURL";
    static final String COL_PROGRESS_URL = "progressURL";
    static final String COL_PLANNED_URL = "plannedURL";
    static final String COL_TEMP_URL = "temporaryURL";
    static final String COL_DEPART = "depart";
    static final String COL_ARRIVE = "arrive";
    static final String COL_QUERY_IS_LEAVE_AFTER = "queryIsLeaveAfter";
    static final String COL_CALORIES_COST = "caloriesCost";
    static final String COL_MAIN_SEGMENT_HASH_CODE = "mainSegmentHashCode";
    static final String COL_MONEY_COST = "moneyCost";
    static final String COL_CARBON_COST = "carbonCost";
    static final String COL_HASSLE_COST = "hassleCost";
    static final String COL_WEIGHTED_SCORE = "weightedScore";
    static final String COL_TRIP_ID = "tripId";
    static final String COL_JSON = "json";
    static final String TABLE_SEGMENTS = "segments";
    static final String SELECT_SEGMENTS = "select * from " + TABLE_SEGMENTS + " where tripId = ?";
    static final String TABLE_TRIPS = "trips";
    static final String SELECT_TRIPS = "select * from " + TABLE_TRIPS + " where " + COL_GROUP_ID + " = ?";
    static final String TABLE_TRIP_GROUPS = "tripGroups";
    static final String COL_IS_NOTIFIABLE = "isNotifiable";
    static final String COL_SOURCES = "sources";

    private RouteContract() {
    }

    static void createTables(SQLiteDatabase database) {
        final DatabaseField frequency = new DatabaseField(COL_FREQUENCY, "integer");
        final DatabaseField displayTripId = new DatabaseField(COL_DISPLAY_TRIP_ID, "integer");
        final DatabaseField depart = new DatabaseField(COL_DEPART, "integer");
        final DatabaseField arrive = new DatabaseField(COL_ARRIVE, "integer");
        final DatabaseField queryIsLeaveAfter = new DatabaseField(COL_QUERY_IS_LEAVE_AFTER, "integer");
        final DatabaseField caloriesCost = new DatabaseField(COL_CALORIES_COST, "real");
        final DatabaseField mainSegmentHashCode = new DatabaseField(COL_MAIN_SEGMENT_HASH_CODE, "integer");
        final DatabaseField tempUrl = new DatabaseField(COL_TEMP_URL, "text");
        final DatabaseField plannedUrl = new DatabaseField(COL_PLANNED_URL, "text");
        final DatabaseField progressUrl = new DatabaseField(COL_PROGRESS_URL, "text");
        final DatabaseField moneyCost = new DatabaseField(COL_MONEY_COST, "real");
        final DatabaseField carbonCost = new DatabaseField(COL_CARBON_COST, "real");
        final DatabaseField updateUrl = new DatabaseField(COL_UPDATE_URL, "text");
        final DatabaseField logUrl = new DatabaseField(COL_LOG_URL, "text");
        final DatabaseField shareUrl = new DatabaseField(COL_SHARE_URL, "text");
        final DatabaseField saveUrl = new DatabaseField(COL_SAVE_URL, "text");
        final DatabaseField currencySymbol = new DatabaseField(COL_CURRENCY_SYMBOL, "text");
        final DatabaseField hassleCost = new DatabaseField(COL_HASSLE_COST, "real");
        final DatabaseField weightedScore = new DatabaseField(COL_WEIGHTED_SCORE, "real");
        final DatabaseField tripId = new DatabaseField(COL_TRIP_ID, "text");
        final DatabaseField _id = new DatabaseField("_id", "integer", "primary key autoincrement");
        final DatabaseField id = new DatabaseField(COL_ID, "integer");
        final DatabaseField groupId = new DatabaseField(COL_GROUP_ID, "text");
        final DatabaseField json = new DatabaseField(COL_JSON, "text");
        final DatabaseField uuid = new DatabaseField(COL_UUID, "text");
        final DatabaseField requestId = new DatabaseField(COL_REQUEST_ID, "text");
        final DatabaseField isNotifiable = new DatabaseField(COL_IS_NOTIFIABLE, "integer");
        final DatabaseField sources = new DatabaseField(COL_SOURCES, "text");
        final DatabaseTable tripGroups = new DatabaseTable(
                TABLE_TRIP_GROUPS,
                new DatabaseField[]{
                        _id, requestId, uuid, frequency, displayTripId, isNotifiable, sources
                },
                UniqueIndices.of(TABLE_TRIP_GROUPS, requestId, uuid),
                "CREATE TRIGGER deleteTrips AFTER DELETE ON " + TABLE_TRIP_GROUPS + " BEGIN " +
                        "DELETE FROM " + TABLE_TRIPS + " WHERE " + groupId + " = old.uuid;" +
                        "END;"
        );
        tripGroups.create(database);

        final DatabaseTable trips = new DatabaseTable(
                TABLE_TRIPS,
                new DatabaseField[]{
                        _id, id, groupId, uuid,
                        currencySymbol, saveUrl, depart, arrive,
                        caloriesCost, moneyCost, carbonCost, hassleCost, weightedScore,
                        updateUrl, progressUrl, plannedUrl, tempUrl,
                        queryIsLeaveAfter, logUrl, shareUrl, mainSegmentHashCode
                },
                UniqueIndices.of(TABLE_TRIPS, id, groupId, uuid),
                "CREATE TRIGGER deleteSegments AFTER DELETE ON " + TABLE_TRIPS + " BEGIN " +
                        "DELETE FROM " + TABLE_SEGMENTS + " WHERE " + tripId + " = old.uuid;" +
                        "END;"
        );
        trips.create(database);

        final DatabaseTable segments = new DatabaseTable(
                TABLE_SEGMENTS,
                new DatabaseField[]{_id, tripId, json}
        );
        segments.create(database);
    }
}
