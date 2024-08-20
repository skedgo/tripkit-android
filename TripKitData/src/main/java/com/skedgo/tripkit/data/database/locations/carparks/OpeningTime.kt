package com.skedgo.tripkit.data.database.locations.carparks

import com.google.gson.annotations.JsonAdapter

import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@Immutable
@TypeAdapters
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersOpeningTime::class)
interface OpeningTime {
    fun closes(): String
    fun opens(): String
}
