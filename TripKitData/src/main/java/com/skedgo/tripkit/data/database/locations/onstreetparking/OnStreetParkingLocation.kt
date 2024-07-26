package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.routing.ModeInfo
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersOnStreetParkingLocation::class)
interface OnStreetParkingLocation {
    fun name(): String
    fun modeInfo(): ModeInfo
    fun onStreetParking(): OnStreetParking
    fun id(): String
    fun lat(): Double
    fun lng(): Double
    fun address(): String?
}