package com.skedgo.tripkit.common.model

import android.os.Parcel
import android.os.Parcelable

class PoiLocation : Location {
    constructor() : super()
    constructor(location: Location, placeId: String? = null) : super(location) {
        this.placeId = placeId
    }

    var placeId: String? = null
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(placeId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PoiLocation> {
        override fun createFromParcel(parcel: Parcel): PoiLocation {
            val location = Location.CREATOR.createFromParcel(parcel)
            val placeId = parcel.readString()
            return PoiLocation(location, placeId)
        }

        override fun newArray(size: Int): Array<PoiLocation> {
            return Array(size) { PoiLocation() }
        }
    }
}