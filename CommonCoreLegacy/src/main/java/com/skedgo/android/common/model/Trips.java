package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

public final class Trips {
  private Trips() {}

  /* Get departure time of a trip null-safely. */
  @Nullable public static String getDepartureTimezone(@Nullable Trip trip) {
    if (trip == null) {
      return null;
    }

    final Location departure = trip.getFrom();
    return departure == null ? null : departure.getTimeZone();
  }
}