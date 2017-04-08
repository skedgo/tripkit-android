package com.skedgo.android.tripkit;

import com.google.android.gms.maps.model.LatLng;
import com.skedgo.android.common.model.Trip;

/**
 * Represents a segment of a polyline denoting a {@link Trip}.
 */
public final class LineSegment {
  public static final int LARGE_DASH = 0;
  public static final int SMALL_DASH = 0;
  public static final int SOLID = 0;
  public final LatLng start;
  public final LatLng end;
  public final int color;
  public final int type;

  public LineSegment(LatLng start, LatLng end, int type, int color) {
    this.start = start;
    this.end = end;
    this.color = color;
    this.type = type;
  }
}