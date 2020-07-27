package com.skedgo.tripkit.data.database.locations.carrentals

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.routing.ModeInfo
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersCarRentalLocation::class)
interface CarRentalLocation {
    fun address(): String?
    fun carRental(): CarRentalInfo
    fun id(): String
    fun lat(): Double
    fun lng(): Double
    fun name(): String?
    fun modeInfo(): ModeInfo
}