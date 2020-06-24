package com.skedgo.tripkit.data.database.locations.freefloating

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(GsonAdaptersFreeFloatingVehicle::class)
interface FreeFloatingVehicle {
    fun available(): Boolean
    fun name(): String
    fun vehicleType(): String
    fun operator(): FreeFloatingOperator
}
