package skedgo.tripkit.routing

import androidx.annotation.Nullable
import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Gson.TypeAdapters()
@Value.Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(value = GsonAdaptersVehicleComponent::class)
abstract class VehicleComponent {
  abstract fun occupancy(): String?

  @Value.Default
  open fun wifi(): Boolean {
    return false
  }

  @Value.Default
  open fun airConditioned(): Boolean {
    return false
  }

  @Value.Default
  open fun wheelchairAccessible(): Boolean {
    return false
  }

  abstract fun model(): String?

}