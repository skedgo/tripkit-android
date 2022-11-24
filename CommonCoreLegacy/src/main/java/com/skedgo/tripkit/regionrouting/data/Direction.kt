package com.skedgo.tripkit.regionrouting.data

import android.os.Parcel
import android.os.Parcelable
import com.skedgo.tripkit.common.util.KParcelable
import com.skedgo.tripkit.common.util.parcelableCreator
import com.skedgo.tripkit.routing.ModeInfo
import com.skedgo.tripkit.routing.ServiceColor
import kotlinx.android.parcel.Parcelize

data class Direction(
        val encodedShape: String?,
        val id: String?,
        val name: String?,
        val shapeIsDetailed: Boolean,
        val stops: List<Stop>?
) : KParcelable {

    private constructor(p: Parcel) : this(
            encodedShape = p.readString(),
            id = p.readString(),
            name = p.readString(),
            shapeIsDetailed = p.readBoolean(),
            stops = kotlin.run {
                val stops = listOf<Stop>()
                p.readTypedList(stops, Stop.CREATOR)
                stops
            }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(encodedShape)
        dest.writeString(id)
        dest.writeString(name)
        dest.writeBoolean(shapeIsDetailed)
        dest.writeTypedList(stops)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Direction)
    }
}