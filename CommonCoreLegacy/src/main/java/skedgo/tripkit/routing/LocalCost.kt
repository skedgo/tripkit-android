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
  val CREATOR: Parcelable.Creator<LocalCost> = object : Parcelable.Creator<LocalCost> {
    override fun createFromParcel(`in`: Parcel): LocalCost {
      return ImmutableLocalCost.builder()
          .minCost(`in`.readFloat().takeIf { it >= 0 })
          .maxCost(`in`.readFloat().takeIf { it >= 0 })
          .cost(`in`.readFloat())
          .accuracy(`in`.readString().let { LocalCostAccuracy.valueOf(it) })
          .currency(`in`.readString())
          .build()
    }

    override fun newArray(size: Int): Array<LocalCost?> {
      return arrayOfNulls(size)
    }
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeFloat(this.minCost() ?: -1f)
    dest.writeFloat(this.maxCost() ?: -1f)
    dest.writeFloat(this.cost())
    dest.writeString(this.accuracy().name)
    dest.writeString(this.currency())
  }

  override fun describeContents(): Int = 0

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