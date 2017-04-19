package com.skedgo.android.common.model;

import android.support.annotation.Nullable;

import skedgo.tripkit.routing.TripExtensionsKt;

/**
 * Use extension functions in {@link TripExtensionsKt} instead.
 */
@Deprecated
public final class Trips {
  private Trips() {}

  /**
   * Get departure time of a trip null-safely.
   * <p>
   * Use {@link TripExtensionsKt#startDateTime(Trip)} instead.
   */
  @Deprecated
  @Nullable public static String getDepartureTimezone(@Nullable Trip trip) {
    if (trip == null) {
      return null;
    }

    final Location departure = trip.getFrom();
    return departure == null ? null : departure.getTimeZone();
  }
}