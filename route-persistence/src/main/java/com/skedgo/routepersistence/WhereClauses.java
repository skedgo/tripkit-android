package com.skedgo.routepersistence;

import android.support.annotation.NonNull;
import android.util.Pair;

import skedgo.tripkit.routing.TripGroup;

import java.util.concurrent.TimeUnit;

import static com.skedgo.routepersistence.RouteContract.COL_ARRIVE;
import static com.skedgo.routepersistence.RouteContract.COL_DISPLAY_TRIP_ID;
import static com.skedgo.routepersistence.RouteContract.COL_GROUP_ID;
import static com.skedgo.routepersistence.RouteContract.COL_ID;
import static com.skedgo.routepersistence.RouteContract.COL_UUID;
import static com.skedgo.routepersistence.RouteContract.TABLE_TRIPS;
import static com.skedgo.routepersistence.RouteContract.TABLE_TRIP_GROUPS;

public final class WhereClauses {
  private WhereClauses() {}

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

  public static Pair<String, String[]> matchesUuidOf(@NonNull TripGroup group) {
    return Pair.create(COL_UUID + " = ?", new String[] {group.uuid()});
  }
}
