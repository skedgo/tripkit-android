package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.data.database.locations.carparks.ParkingOperator
import com.skedgo.tripkit.parkingspots.models.Parking
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersOnStreetParking::class)
interface OnStreetParking {
    fun description(): String
    fun encodedPolyline(): String
    fun parkingVacancy(): Parking.Vacancy
    fun availableContent(): Array<String>
    fun source(): ParkingProvider

}


@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersParkingProvider::class)
interface ParkingProvider {
    fun provider(): ParkingOperator
}