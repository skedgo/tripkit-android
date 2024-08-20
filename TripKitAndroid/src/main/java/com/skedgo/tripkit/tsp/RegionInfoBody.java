package com.skedgo.tripkit.tsp;

import com.google.gson.annotations.JsonAdapter;
import com.skedgo.tripkit.common.model.Region;

import org.immutables.value.Value;

import static org.immutables.gson.Gson.TypeAdapters;
import static org.immutables.value.Value.Immutable;
import static org.immutables.value.Value.Style;

@Immutable
@TypeAdapters
@Style(passAnnotations = JsonAdapter.class)
@JsonAdapter(GsonAdaptersRegionInfoBody.class)
public interface RegionInfoBody {
    /**
     * @return {@link Region#getName()}.
     */
    @Value.Parameter
    String region();
}