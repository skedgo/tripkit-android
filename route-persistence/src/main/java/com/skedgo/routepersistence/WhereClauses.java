package com.skedgo.routepersistence;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.skedgo.android.common.model.TripGroup;

import java.util.concurrent.TimeUnit;

import static com.skedgo.routepersistence.TripGroupContract.*;

public final class WhereClauses {
  private WhereClauses() {}

  /**
   * Creates where clause to match trips that happened before given amount of hours.
   *
   * @param currentMillis Should be {@link System#currentTimeMillis()}.
   */
  public static Pair<String, String[]> happenedBefore(long hours, long currentMillis) {
    final String where = "EXISTS ("
        + "SELECT * FROM " + TABLE_TRIPS + " JOIN " + TABLE_TRIP_GROUPS
        + " ON " + TABLE_TRIP_GROUPS + "." + COL_UUID + " = " + TABLE_TRIPS + "." + COL_GROUP_ID
        + " AND " + TABLE_TRIP_GROUPS + "." + COL_DISPLAY_TRIP_ID + " = " + TABLE_TRIPS + "." + COL_ID
        + " JOIN " + RouteContract.ROUTES + " ON " + RouteContract.TRIP_GROUP_ID + " = " + TABLE_TRIP_GROUPS + "." + COL_UUID
        + " WHERE " + TABLE_TRIPS + "." + COL_ARRIVE + " < ?"
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
