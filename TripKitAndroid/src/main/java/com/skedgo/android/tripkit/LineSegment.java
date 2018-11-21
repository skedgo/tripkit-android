package com.skedgo.android.tripkit;

import com.google.android.gms.maps.model.LatLng;
import skedgo.tripkit.routing.Trip;

/**
 * Represents a segment of a polyline denoting a {@link Trip}.
 */
public final class LineSegment {
  public final LatLng start;
  public final LatLng end;
  public final int color;

  public LineSegment(LatLng start, LatLng end, int color) {
    this.start = start;
    this.end = end;
    this.color = color;
  }
}