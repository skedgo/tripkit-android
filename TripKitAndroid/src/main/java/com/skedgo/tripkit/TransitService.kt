package com.skedgo.tripkit

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.routing.RealTimeVehicle
import com.skedgo.tripkit.routing.Shape
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@JsonAdapter(
    GsonAdaptersTransitService::class
)
@Style(passAnnotations = [JsonAdapter::class])
interface TransitService {
    fun shapes(): List<Shape>

    fun realTimeStatus(): String

    fun realtimeVehicle(): RealTimeVehicle

    fun realtimeAlternativeVehicle(): List<RealTimeVehicle>
}