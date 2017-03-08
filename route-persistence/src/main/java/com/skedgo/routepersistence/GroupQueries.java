package com.skedgo.routepersistence;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.TripGroup;

import java.util.UUID;

import static com.skedgo.routepersistence.TripGroupContract.*;

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
  static Pair<String, String[]> hasRequestId(@NonNull String id) {
    return Pair.create(
        RouteContract.ROUTE_ID + " = ?",
        new String[] {id}
    );
  }

  /**
   * @param uuid Should be {@link TripGroup#uuid()}.
   */
  static Pair<String, String[]> hasUuid(@NonNull String uuid) {
    return Pair.create(
        COL_UUID + " = ?",
        new String[] {uuid}
    );
  }
}
