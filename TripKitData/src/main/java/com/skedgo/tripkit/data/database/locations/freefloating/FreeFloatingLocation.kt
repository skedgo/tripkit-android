package com.skedgo.tripkit.data.database.locations.freefloating

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.routing.ModeInfo
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersFreeFloatingLocation::class)
interface FreeFloatingLocation {
    fun address(): String?
    fun id(): String
    fun lat(): Double
    fun lng(): Double
    fun name(): String?
    fun modeInfo(): ModeInfo
    fun vehicle(): FreeFloatingVehicle
}
