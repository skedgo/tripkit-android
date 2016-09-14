package com.skedgo.android.tripkit.tsp;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.android.common.model.Region;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersRegionInfoBody.class)
public interface RegionInfoBody {
  /**
   * @return {@link Region#getName()}.
   */
  @Value.Parameter String region();
}