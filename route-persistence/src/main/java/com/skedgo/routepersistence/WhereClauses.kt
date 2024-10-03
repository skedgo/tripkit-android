package com.skedgo.routepersistence

import android.util.Pair
import com.skedgo.routepersistence.RouteContract.COL_ARRIVE
import com.skedgo.routepersistence.RouteContract.COL_DISPLAY_TRIP_ID
import com.skedgo.routepersistence.RouteContract.COL_GROUP_ID
import com.skedgo.routepersistence.RouteContract.COL_ID
import com.skedgo.routepersistence.RouteContract.COL_TRIP_ID
import com.skedgo.routepersistence.RouteContract.COL_UUID
import com.skedgo.routepersistence.RouteContract.TABLE_SEGMENTS
import com.skedgo.routepersistence.RouteContract.TABLE_TRIPS
import com.skedgo.routepersistence.RouteContract.TABLE_TRIP_GROUPS
import com.skedgo.tripkit.routing.TripGroup
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS

object WhereClauses {
    /**
     * Creates where clause to match trips that happened before given amount of hours.
     *
     * @param currentMillis Should be [System.currentTimeMillis].
     */
    @JvmStatic
    fun happenedBefore(hours: Long, currentMillis: Long): Pair<String, Array<String>> {
        val where = ("EXISTS ("
            + "SELECT * FROM " + TABLE_TRIPS
            + " WHERE " + TABLE_TRIP_GROUPS + "." + COL_UUID + " = " + TABLE_TRIPS + "." + COL_GROUP_ID
            + " AND " + TABLE_TRIP_GROUPS + "." + COL_DISPLAY_TRIP_ID + " = " + TABLE_TRIPS + "." + COL_ID
            + " AND " + TABLE_TRIPS + "." + COL_ARRIVE + " < ?"
            + ")")
        val secs = HOURS.toSeconds(hours)
        val currentSecs = MILLISECONDS.toSeconds(currentMillis)
        val args = arrayOf((currentSecs - secs).toString())
        return Pair.create(where, args)
    }

    // Remove tripsGroups in routes.db that is before the given current dateTime (in millis) - hours
    fun removeTripGroupsHappenedBefore(
        hours: Long,
        currentMillis: Long
    ): Pair<String, Array<String>> {
        val secs = HOURS.toSeconds(hours)
        val currentSecs = MILLISECONDS.toSeconds(currentMillis)
        val args = arrayOf((currentSecs - secs).toString())
        val where = ("EXISTS ("
            + "SELECT * FROM " + TABLE_TRIPS
            + " WHERE " + TABLE_TRIP_GROUPS + "." + COL_UUID + " = " + TABLE_TRIPS + "." + COL_GROUP_ID
            + " AND " + TABLE_TRIPS + "." + COL_ARRIVE + " < ?"
            + ")")
        return Pair(where, args)
    }

    // Remove all trips that doesn't have a group in tripGroups table
    fun removeTripsWithNoTripGroup(): Pair<String, Array<String>> {
        val where: String =
            "$COL_GROUP_ID NOT IN (SELECT $COL_UUID FROM $TABLE_TRIP_GROUPS)"
        return Pair(where, arrayOf())
    }

    // Remove all segments that doesn't have a trip in trips table
    fun removeSegmentsWithNoTrip(): Pair<String, Array<String>> {
        val where: String =
            "$COL_TRIP_ID NOT IN (SELECT $COL_UUID FROM $TABLE_TRIPS)"
        return Pair(where, arrayOf())
    }

    val tripGroupsTable: String
        get() = TABLE_TRIP_GROUPS

    val tripsTable: String
        get() = TABLE_TRIPS

    val segmentsTable: String
        get() = TABLE_SEGMENTS

    @JvmStatic
    fun matchesUuidOf(group: TripGroup): Pair<String, Array<String>> {
        return Pair.create<String, Array<String>>("$COL_UUID = ?", arrayOf<String>(group.uuid()))
    }
}
