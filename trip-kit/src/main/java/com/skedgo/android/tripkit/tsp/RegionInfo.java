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

  @Value.Default
  public boolean transitWheelchairAccessibility() { return false; }

  @Value.Default
  public boolean supportsConcessionPricing() { return false; }
}