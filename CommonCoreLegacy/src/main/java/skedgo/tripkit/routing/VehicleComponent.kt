package skedgo.tripkit.routing

import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters()
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(value = GsonAdaptersVehicleComponent::class)
interface VehicleComponent {
  fun occupancy(): Occupancy
  fun wifi(): Boolean
  fun airConditioned(): Boolean
  fun wheelchairAccessible(): Boolean
  fun model(): String
}