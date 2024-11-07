package com.skedgo.tripkit

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.common.model.stop.ScheduledStop
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersLocationInfo::class
)
interface LocationInfo {
    fun details(): LocationInfoDetails?

    fun stop(): ScheduledStop?

    fun carPark(): CarPark?

    fun lat(): Double

    fun lng(): Double
}