package skedgo.tripkit.routing

import androidx.annotation.Keep
import com.google.gson.annotations.JsonAdapter
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Value.Style(passAnnotations = [JsonAdapter::class])
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersLocalCost::class)
interface LocalCost {
  /**
   * @return Minimum value for when the price is within a range
   */
  fun minCost(): Float?

  /**
   * @return Maximum value for when the price is within a range
   */
  fun maxCost(): Float?

  /**
   * @return Cost of this segment in local currency (it's an average for ranges, considering quartile info)
   */
  fun cost(): Float

  fun accuracy(): LocalCostAccuracy

  /**
   * @return The ISO 4217 currency code
   */
  fun currency(): String
}

enum class LocalCostAccuracy {
  Internal_Estimate,
  External_Estimate,
  Confirmed
}