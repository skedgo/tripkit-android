package com.skedgo.android.tripkit.tsp;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import skedgo.tripkit.routing.ModeInfo;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersRegionInfo.class)
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
  public boolean streetWheelchairAccessibility() {return false;}

  @Value.Default
  public boolean supportsConcessionPricing() { return false; }
}