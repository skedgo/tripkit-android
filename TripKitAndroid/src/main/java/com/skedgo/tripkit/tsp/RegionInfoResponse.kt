package com.skedgo.tripkit.tsp

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.data.tsp.RegionInfo
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersRegionInfoResponse::class
)
interface RegionInfoResponse {
    fun regions(): List<RegionInfo>
}