package com.skedgo.tripkit.data.database.locations.carparks

import com.google.gson.annotations.JsonAdapter

import com.skedgo.tripkit.routing.ModeInfo

import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersCarParkLocation::class)
interface CarParkLocation {
    fun name(): String
    fun modeInfo(): ModeInfo
    fun carPark(): CarPark
    fun id(): String
    fun lat(): Double
    fun lng(): Double
    fun address(): String?
}