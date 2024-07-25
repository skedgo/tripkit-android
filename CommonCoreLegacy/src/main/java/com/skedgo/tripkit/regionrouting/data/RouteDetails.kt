package com.skedgo.tripkit.regionrouting.data

import android.os.Parcel
import com.skedgo.tripkit.common.util.KParcelable
import com.skedgo.tripkit.common.util.parcelableCreator
import com.skedgo.tripkit.routing.ModeInfo
import com.skedgo.tripkit.routing.ServiceColor

data class RouteDetails(
    val directions: List<Direction>?,
    val id: String?,
    val mode: String?,
    val modeInfo: ModeInfo?,
    val operatorId: String?,
    val operatorName: String?,
    val region: String?,
    val routeColor: ServiceColor?,
    val routeName: String?,
    val shortName: String?,
    val routeDescription: String?,
) : KParcelable {

    private constructor(p: Parcel) : this(
        directions = kotlin.run {
            val directions = listOf<Direction>()
            p.readTypedList(directions, Direction.CREATOR)
            directions
        },
        id = p.readString(),
        mode = p.readString(),
        modeInfo = p.readParcelable<ModeInfo>(ModeInfo::class.java.classLoader),
        operatorId = p.readString(),
        operatorName = p.readString(),
        region = p.readString(),
        routeColor = p.readParcelable<ServiceColor>(ServiceColor::class.java.classLoader),
        routeName = p.readString(),
        shortName = p.readString(),
        routeDescription = p.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(directions)
        dest.writeString(id)
        dest.writeString(mode)
        dest.writeParcelable(modeInfo, 0)
        dest.writeString(operatorId)
        dest.writeString(operatorName)
        dest.writeString(region)
        dest.writeParcelable(routeColor, 0)
        dest.writeString(routeName)
        dest.writeString(shortName)
        dest.writeString(routeDescription)
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::RouteDetails)
    }
}