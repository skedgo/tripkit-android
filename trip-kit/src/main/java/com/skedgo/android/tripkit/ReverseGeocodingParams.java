package com.skedgo.android.tripkit;

import org.immutables.value.Value;

@Value.Immutable
public abstract class ReverseGeocodingParams {
  public abstract double lat();
  public abstract double lng();
  public abstract int maxResults();
}