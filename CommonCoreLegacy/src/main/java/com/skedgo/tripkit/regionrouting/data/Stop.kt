package com.skedgo.tripkit.regionrouting.data

import android.os.Parcel
import com.skedgo.tripkit.common.util.KParcelable
import com.skedgo.tripkit.common.util.parcelableCreator

data class Stop(
    val common: Boolean,
    val lat: Int,
    val lng: Int,
    val name: String?,
    val stopCode: String?
) : KParcelable {

    private constructor(p: Parcel) : this(
        common = p.readBoolean(),
        lat = p.readInt(),
        lng = p.readInt(),
        name = p.readString(),
        stopCode = p.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeBoolean(common)
        dest.writeInt(lat)
        dest.writeInt(lng)
        dest.writeString(name)
        dest.writeString(stopCode)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Stop)
    }
}