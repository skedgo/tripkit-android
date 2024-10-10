package com.skedgo.tripkit.routing

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.stop.ServiceStop

data class Shape(
    var id: Long = 0,
    @SerializedName("travelled") var isTravelled: Boolean = false,
    @SerializedName("stops") var stops: List<ServiceStop>? = null,
    @SerializedName("serviceColor") var serviceColor: ServiceColor,
    @SerializedName("encodedWaypoints") var encodedWaypoints: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        isTravelled = parcel.readInt() == 1,
        stops = parcel.readArrayList(ServiceStop::class.java.classLoader) as? List<ServiceStop>,
        encodedWaypoints = parcel.readString() ?: "",  // Non-null default value for encodedWaypoints
        serviceColor = parcel.readParcelable(ServiceColor::class.java.classLoader)
            ?: throw IllegalStateException("ServiceColor must not be null")  // Ensure non-nullable
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeInt(if (isTravelled) 1 else 0)
        parcel.writeList(stops)
        parcel.writeString(encodedWaypoints)
        parcel.writeParcelable(serviceColor, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Shape> {
        override fun createFromParcel(parcel: Parcel): Shape {
            return Shape(parcel)
        }

        override fun newArray(size: Int): Array<Shape?> {
            return arrayOfNulls(size)
        }
    }
}