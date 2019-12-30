package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersOnStreetParkingDetailsDto::class)
interface OnStreetParkingDetailsDto {
  fun actualRate(): String?
  fun availableSpaces(): Int?
  fun totalSpaces(): Int?
  fun lastUpdate(): Long?
  fun restrictions(): List<RestrictionDto>?
  fun paymentTypes(): List<String>?
}




@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersOnStreetParkingLocationDto::class)
interface OnStreetParkingLocationDto {
  fun onStreetParking() : OnStreetParkingDetailsDto
}



