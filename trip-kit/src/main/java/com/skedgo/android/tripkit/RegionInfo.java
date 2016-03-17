package com.skedgo.android.tripkit;

import android.support.annotation.Nullable;

import com.skedgo.android.common.model.ModeInfo;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

import static org.immutables.value.Value.Style.BuilderVisibility.PACKAGE;

@Gson.TypeAdapters
@Value.Immutable
@Value.Style(
    visibility = Value.Style.ImplementationVisibility.PACKAGE,
    builderVisibility = PACKAGE
)
public abstract class RegionInfo {
  @Nullable public abstract List<ModeInfo> transitModes();
  @Nullable public abstract Paratransit paratransit();

  @Value.Default public boolean supportsConcessionPricing() {
    return false;
  }
}