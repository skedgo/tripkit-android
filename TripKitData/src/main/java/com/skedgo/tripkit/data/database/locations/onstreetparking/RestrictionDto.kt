package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersRestrictionDto::class)
interface RestrictionDto {

    fun color(): String
    fun maximumParkingMinutes(): Int?
    fun parkingSymbol(): String
    fun type(): String
    fun daysAndTimes(): RestrictionDayAndTimeDto

}