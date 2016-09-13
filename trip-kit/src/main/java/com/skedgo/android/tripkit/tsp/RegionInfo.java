package com.skedgo.android.tripkit.tsp;

import android.support.annotation.Nullable;

import com.skedgo.android.common.model.ModeInfo;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Gson.TypeAdapters
public abstract class RegionInfo {
  @Nullable public abstract List<ModeInfo> transitModes();
  @Nullable public abstract Paratransit paratransit();

  /**
   * @return If true, indicates that we have wheelchair accessibility
   * information for public transport for the current region.
   * Otherwise, no wheelchair accessibility info.
   */
  @Value.Default
  public boolean transitWheelchairAccessibility() { return false; }

  @Value.Default
  public boolean supportsConcessionPricing() { return false; }
}