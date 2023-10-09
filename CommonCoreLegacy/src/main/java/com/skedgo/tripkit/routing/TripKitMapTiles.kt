package com.skedgo.tripkit.routing

import android.os.Parcel
import android.os.Parcelable

data class TripKitMapTiles(
    val name: String,
    val urlTemplates: List<String>,
    val sources: List<Source>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        mutableListOf<Source>().apply {
            parcel.readList(this, Source::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeStringList(urlTemplates)
        parcel.writeList(sources)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TripKitMapTiles> {
        override fun createFromParcel(parcel: Parcel): TripKitMapTiles {
            return TripKitMapTiles(parcel)
        }

        override fun newArray(size: Int): Array<TripKitMapTiles?> {
            return arrayOfNulls(size)
        }
    }
}
