package com.skedgo.tripkit.data.database.locations.carrentals

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value


@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersCarRentalCompany::class)
interface CarRentalCompany {
    fun name(): String
}