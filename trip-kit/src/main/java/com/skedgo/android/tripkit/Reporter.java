package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skedgo.android.common.model.Trip;

import java.util.Map;

public interface Reporter {
  /**
   * Reports the provided trip as being planned for the user.
   * This is posted to the server with additional optional data.
   * Example use case: Report a trip as being planned, and
   * then later get push notifications about alerts relevant to the trip,
   * or about ride sharing opportunities.
   *
   * @param trip     Trip to report as planned. Note that this only
   *                 does anything if the trip has a {@link Trip#getPlannedURL()}.
   * @param userInfo Arbitrary data which gets POST'ed as JSON.
   */
  void reportPlannedTrip(@NonNull Trip trip, @Nullable Map<String, Object> userInfo);
}