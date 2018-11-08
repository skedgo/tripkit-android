package com.skedgo.routepersistence;

import androidx.annotation.NonNull;
import android.util.Pair;

import com.skedgo.android.common.model.Query;
import skedgo.tripkit.routing.TripGroup;

import java.util.UUID;

import static com.skedgo.routepersistence.RouteContract.COL_IS_NOTIFIABLE;
import static com.skedgo.routepersistence.RouteContract.COL_REQUEST_ID;
import static com.skedgo.routepersistence.RouteContract.COL_UUID;
import static com.skedgo.routepersistence.RouteContract.TABLE_TRIP_GROUPS;

public final class GroupQueries {
  private GroupQueries() {}

  public static Pair<String, String[]> isNotifiable() {
    return Pair.create(
        "select * from " + TABLE_TRIP_GROUPS
            + " where " + COL_IS_NOTIFIABLE + " = 1",
        null
    );
  }

  /**
   * @param id Should be {@link Query#uuid()} or
   *           whatever {@link UUID#toString()} that
   *           {@link TripGroup}s were associated with when saving them.
   */
  public static Pair<String, String[]> hasRequestId(@NonNull String id) {
    return Pair.create(
        "select * from " + TABLE_TRIP_GROUPS
            + " where " + COL_REQUEST_ID + " = ?",
        new String[] {id}
    );
  }

  /**
   * @param uuid Should be {@link TripGroup#uuid()}.
   */
  public static Pair<String, String[]> hasUuid(@NonNull String uuid) {
    return Pair.create(
        "select * from " + TABLE_TRIP_GROUPS
            + " where " + COL_UUID + " = ?",
        new String[] {uuid}
    );
  }
}
