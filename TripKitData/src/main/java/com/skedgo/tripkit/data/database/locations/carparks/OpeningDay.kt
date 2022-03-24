package com.skedgo.tripkit.data.database.locations.carparks

import com.google.gson.annotations.JsonAdapter

import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersOpeningDay::class)
interface OpeningDay {

  fun name(): String
  fun times(): List<OpeningTime>

}