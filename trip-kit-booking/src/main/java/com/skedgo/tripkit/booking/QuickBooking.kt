package com.skedgo.tripkit.booking

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.annotation.Nullable
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value
import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersQuickBooking::class
)
@Deprecated("")
abstract class QuickBooking : Parcelable {

    abstract fun bookingURL(): String?
    abstract fun tripUpdateURL(): String?
    abstract fun imageURL(): String?
    abstract fun title(): String?
    abstract fun subtitle(): String?
    abstract fun bookingTitle(): String?
    abstract fun priceString(): String?

    @Value.Default
    open fun price(): Float {
        return -1f
    }

    @Value.Default
    @SerializedName("USDPrice")
    open fun priceInUSD(): Float {
        return -1f
    }

    @Value.Default
    @SerializedName("ETA")
    open fun eta(): Float {
        return -1f
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(bookingURL())
        dest.writeString(tripUpdateURL())
        dest.writeFloat(priceInUSD())
        dest.writeString(imageURL())
        dest.writeString(title())
        dest.writeString(subtitle())
        dest.writeString(bookingTitle())
        dest.writeString(priceString())
        dest.writeFloat(price())
        dest.writeFloat(eta())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuickBooking> {
        override fun createFromParcel(parcel: Parcel): QuickBooking {
            return ImmutableQuickBooking.builder()
                .bookingURL(parcel.readString())
                .tripUpdateURL(parcel.readString())
                .priceInUSD(parcel.readFloat())
                .imageURL(parcel.readString())
                .title(parcel.readString())
                .subtitle(parcel.readString())
                .bookingTitle(parcel.readString())
                .priceString(parcel.readString())
                .price(parcel.readFloat())
                .eta(parcel.readFloat())
                .build()
        }

        override fun newArray(size: Int): Array<QuickBooking?> {
            return arrayOfNulls(size)
        }
    }
}