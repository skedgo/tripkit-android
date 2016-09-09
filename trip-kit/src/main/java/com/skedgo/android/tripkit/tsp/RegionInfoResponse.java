package com.skedgo.android.tripkit.tsp;

import android.support.annotation.Nullable;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
public interface RegionInfoResponse {
  @Nullable List<RegionInfo> regions();
}