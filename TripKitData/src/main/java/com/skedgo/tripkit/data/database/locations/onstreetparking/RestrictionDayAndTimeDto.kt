package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersRestrictionDayAndTimeDto::class)
interface RestrictionDayAndTimeDto {
    fun days(): List<RestrictionDayDto>
    fun timeZone(): String
}


@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersRestrictionDayDto::class)
interface RestrictionDayDto {
    fun name(): String
    fun times(): List<RestrictionTimeDto>
}

