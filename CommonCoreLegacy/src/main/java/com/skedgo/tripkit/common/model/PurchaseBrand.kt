package com.skedgo.tripkit.common.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.google.gson.annotations.JsonAdapter
import com.skedgo.tripkit.routing.ServiceColor
import org.immutables.gson.Gson.TypeAdapters
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style

@TypeAdapters
@Immutable
@Style(passAnnotations = [JsonAdapter::class])
@JsonAdapter(
    GsonAdaptersPurchaseBrand::class
)
abstract class PurchaseBrand : Parcelable {
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(color(), flags)
        dest.writeString(name())
        dest.writeString(remoteIcon())
    }

    override fun describeContents(): Int {
        return 0
    }

    abstract fun color(): ServiceColor?

    abstract fun name(): String?

    abstract fun remoteIcon(): String?

    companion object {
        val CREATOR: Creator<PurchaseBrand> = object : Creator<PurchaseBrand> {
            override fun createFromParcel(`in`: Parcel): PurchaseBrand? {
                return ImmutablePurchaseBrand.builder()
                    .color(`in`.readParcelable<Parcelable>(ServiceColor::class.java.classLoader) as ServiceColor?)
                    .name(`in`.readString())
                    .remoteIcon(`in`.readString())
                    .build()
            }

            override fun newArray(size: Int): Array<PurchaseBrand?> {
                return arrayOfNulls(size)
            }
        }
    }
}
