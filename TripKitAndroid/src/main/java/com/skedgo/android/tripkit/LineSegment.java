package com.skedgo.android.tripkit;

import com.google.android.gms.maps.model.LatLng;
import com.skedgo.android.common.util.TripKitLatLng;

import skedgo.tripkit.routing.Trip;

/**
 * Represents a segment of a polyline denoting a {@link Trip}.
 */
public final class LineSegment {
  public final TripKitLatLng start;
  public final TripKitLatLng end;
  public final int color;

  public LineSegment(TripKitLatLng start, TripKitLatLng end, int color) {
    this.start = start;
    this.end = end;
    this.color = color;
  }
}