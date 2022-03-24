package com.skedgo.tripkit.data.database.locations.carrentals

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.data.database.locations.carparks.OpeningHours
import org.immutables.gson.Gson
import org.immutables.value.Value


@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersCarRentalInfo::class)
interface CarRentalInfo {
    fun identifier(): String
    fun company(): CarRentalCompany
    fun openingHours(): OpeningHours
}