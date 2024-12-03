package com.skedgo.tripkit.tsp

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Parameter
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersRegionInfoBody::class
)
interface RegionInfoBody {
    /**
     * @return [Region.getName].
     */
    @Parameter
    fun region(): String
}