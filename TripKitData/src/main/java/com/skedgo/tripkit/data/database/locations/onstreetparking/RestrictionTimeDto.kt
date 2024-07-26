package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersRestrictionTimeDto::class)
interface RestrictionTimeDto {
    fun closes(): String
    fun opens(): String
}