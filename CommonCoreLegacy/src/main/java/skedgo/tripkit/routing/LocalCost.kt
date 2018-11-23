package skedgo.tripkit.routing

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.JsonAdapter
import com.skedgo.android.common.model.Booking
import com.skedgo.android.common.model.Location
import com.skedgo.android.common.model.RealtimeAlert
import com.skedgo.android.common.model.Street
import org.immutables.gson.Gson
import org.immutables.value.Value

@Value.Immutable
@Value.Style(passAnnotations = [JsonAdapter::class])
@Gson.TypeAdapters
@JsonAdapter(GsonAdaptersLocalCost::class)
abstract class LocalCost : Parcelable {
  /**
   * @return Minimum value for when the price is within a range
   */
  abstract fun minCost(): Float?

  /**
   * @return Maximum value for when the price is within a range
   */
  abstract fun maxCost(): Float?

  /**
   * @return Cost of this segment in local currency (it's an average for ranges, considering quartile info)
   */
  abstract fun cost(): Float

  abstract fun accuracy(): LocalCostAccuracy

  /**
   * @return The ISO 4217 currency code
   */
  abstract fun currency(): String
}

enum class LocalCostAccuracy {
  Internal_Estimate,
  External_Estimate,
  Confirmed
}