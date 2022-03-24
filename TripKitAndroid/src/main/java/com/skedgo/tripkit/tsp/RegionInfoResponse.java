package com.skedgo.tripkit.tsp;

import androidx.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;

import com.skedgo.tripkit.data.tsp.RegionInfo;

import java.util.List;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@TypeAdapters
@Immutable
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersRegionInfoResponse.class)
public interface RegionInfoResponse {
  @Nullable List<RegionInfo> regions();
}