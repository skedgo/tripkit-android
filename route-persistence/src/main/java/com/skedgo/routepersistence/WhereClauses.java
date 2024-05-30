package com.skedgo.routepersistence;

import androidx.annotation.NonNull;

import android.util.Pair;

import com.skedgo.tripkit.routing.TripGroup;

import java.util.concurrent.TimeUnit;

import static com.skedgo.routepersistence.RouteContract.COL_ARRIVE;
import static com.skedgo.routepersistence.RouteContract.COL_DISPLAY_TRIP_ID;
import static com.skedgo.routepersistence.RouteContract.COL_GROUP_ID;
import static com.skedgo.routepersistence.RouteContract.COL_ID;
import static com.skedgo.routepersistence.RouteContract.COL_TRIP_ID;
import static com.skedgo.routepersistence.RouteContract.COL_UUID;
import static com.skedgo.routepersistence.RouteContract.TABLE_SEGMENTS;
import static com.skedgo.routepersistence.RouteContract.TABLE_TRIPS;
import static com.skedgo.routepersistence.RouteContract.TABLE_TRIP_GROUPS;

public final class WhereClauses {
    private WhereClauses() {
    }

    /**
     * Creates where clause to match trips that happened before given amount of hours.
     *
     * @param currentMillis Should be {@link System#currentTimeMillis()}.
     */
    public static Pair<String, String[]> happenedBefore(long hours, long currentMillis) {
        final String where = "EXISTS ("
                + "SELECT * FROM " + TABLE_TRIPS
                + " WHERE " + TABLE_TRIP_GROUPS + "." + COL_UUID + " = " + TABLE_TRIPS + "." + COL_GROUP_ID
                + " AND " + TABLE_TRIP_GROUPS + "." + COL_DISPLAY_TRIP_ID + " = " + TABLE_TRIPS + "." + COL_ID
                + " AND " + TABLE_TRIPS + "." + COL_ARRIVE + " < ?"
                + ")";
        final long secs = TimeUnit.HOURS.toSeconds(hours);
        final long currentSecs = TimeUnit.MILLISECONDS.toSeconds(currentMillis);
        final String[] args = {String.valueOf(currentSecs - secs)};
        return Pair.create(where, args);
    }

    // Remove tripsGroups in routes.db that is before the given current dateTime (in millis) - hours
    public static Pair<String, String[]> removeTripGroupsHappenedBefore(long hours, long currentMillis) {
        long secs = TimeUnit.HOURS.toSeconds(hours);
        long currentSecs = TimeUnit.MILLISECONDS.toSeconds(currentMillis);
        String[] args = new String[] { String.valueOf(currentSecs - secs) };
        String where = "EXISTS ("
                + "SELECT * FROM " + TABLE_TRIPS
                + " WHERE " + TABLE_TRIP_GROUPS + "." + COL_UUID + " = " + TABLE_TRIPS + "." + COL_GROUP_ID
                + " AND " + TABLE_TRIPS + "." + COL_ARRIVE + " < ?"
                + ")";
        return new Pair<>(where, args);
    }

    // Remove all trips that doesn't have a group in tripGroups table
    public static Pair<String, String[]> removeTripsWithNoTripGroup() {
        String where = COL_GROUP_ID + " NOT IN (SELECT " + COL_UUID + " FROM " + TABLE_TRIP_GROUPS + ")";
        return new Pair<>(where, new String[] {});
    }

    // Remove all segments that doesn't have a trip in trips table
    public static Pair<String, String[]> removeSegmentsWithNoTrip() {
        String where = COL_TRIP_ID + " NOT IN (SELECT " + COL_UUID + " FROM " + TABLE_TRIPS + ")";
        return new Pair<>(where, new String[] {});
    }

    public static String getTripGroupsTable() {
        return TABLE_TRIP_GROUPS;
    }

    public static String getTripsTable() {
        return TABLE_TRIPS;
    }

    public static String getSegmentsTable() {
        return TABLE_SEGMENTS;
    }

    public static Pair<String, String[]> matchesUuidOf(@NonNull TripGroup group) {
        return Pair.create(COL_UUID + " = ?", new String[]{group.uuid()});
    }
}
