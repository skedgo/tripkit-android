package com.skedgo.tripkit.common.model.stop

import android.os.Parcel
import android.os.Parcelable.Creator
import android.text.TextUtils
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.R
import com.skedgo.tripkit.common.model.location.Location
import com.skedgo.tripkit.common.model.stop.StopType.BUS
import com.skedgo.tripkit.common.model.stop.StopType.CABLECAR
import com.skedgo.tripkit.common.model.stop.StopType.Companion.from
import com.skedgo.tripkit.common.model.stop.StopType.FERRY
import com.skedgo.tripkit.common.model.stop.StopType.MONORAIL
import com.skedgo.tripkit.common.model.stop.StopType.PARKING
import com.skedgo.tripkit.common.model.stop.StopType.SUBWAY
import com.skedgo.tripkit.common.model.stop.StopType.TRAIN
import com.skedgo.tripkit.common.model.stop.StopType.TRAM
import com.skedgo.tripkit.routing.ModeInfo
import com.skedgo.tripkit.routing.VehicleMode

class ScheduledStop : Location {
    var stopId: Long = 0

    @SerializedName("code")
    var code: String? = null

    @SerializedName("stop_code")
    var endStopCode: String? = null

    @SerializedName("services")
    var services: String? = null

    @SerializedName("children")
    var children: ArrayList<ScheduledStop>? = null

    @SerializedName("shortName")
    var shortName: String? = null

    @SerializedName("stopType")
    var type: StopType? = null

    @SerializedName("modeInfo")
    var modeInfo: ModeInfo? = null

    @Transient
    var parentId: Long = 0
        private set

    @Transient
    var currentFilter: String? = null

    @SerializedName("wheelchairAccessible")
    var wheelchairAccessible: Boolean? = null

    @SerializedName("bicycleAccessible")
    var bicycleAccessible: Boolean? = null

    @SerializedName("alertHashCodes")
    var alertHashCodes: ArrayList<Long>? = null

    constructor() : super()

    constructor(location: Location?) : super(location)

    override fun fillFrom(location: Location?) {
        if (location == null) {
            return
        }

        super.fillFrom(location)
        if (location is ScheduledStop) {
            val other = location
            stopId = other.stopId
            this.code = other.code
            endStopCode = other.endStopCode
            services = other.services
            children = other.children
            shortName = other.shortName
            type = other.type
            modeInfo = other.modeInfo
            parentId = other.parentId
            currentFilter = other.currentFilter
            wheelchairAccessible = other.wheelchairAccessible
            bicycleAccessible = other.bicycleAccessible
            alertHashCodes = other.alertHashCodes
        }
    }

    fun hasChildren(): Boolean {
        return children != null && !children!!.isEmpty()
    }

    val isParent: Boolean
        /**
         * Alias of [.hasChildren]. If a stop has children, it's a parent stop.
         */
        get() = hasChildren()

    override var locationType: Int
        get() = TYPE_SCHEDULED_STOP
        set(locationType) {
            super.locationType = locationType
        }

    val embarkationStopCode: List<String>
        get() {
            val list = ArrayList<String?>()
            list.add(this.code)
            return list.filterNotNull()
        }

    val disembarkationStopCode: List<String>?
        get() {
            if (endStopCode != null) {
                val list = ArrayList<String?>()
                list.add(endStopCode)
                return list.filterNotNull()
            }
            return null
        }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }

        if (obj != null && (obj is ScheduledStop)) {
            return TextUtils.equals(this.code, obj.code)
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return if (this.code != null) code.hashCode() else 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)

        out.writeLong(stopId)
        out.writeString(this.code)
        out.writeString(endStopCode)
        out.writeList(children)
        out.writeLong(parentId)
        out.writeString(shortName)
        out.writeString(services)
        out.writeString(currentFilter)
        out.writeString(if (type == null) null else type.toString())
        out.writeParcelable(modeInfo, 0)
        out.writeValue(wheelchairAccessible)
        out.writeValue(bicycleAccessible)
        out.writeList(alertHashCodes)
    }

    companion object {
        @JvmField
        val CREATOR: Creator<ScheduledStop> = object : Creator<ScheduledStop> {
            override fun createFromParcel(`in`: Parcel): ScheduledStop {
                val location = Location.CREATOR.createFromParcel(`in`)

                val stop = ScheduledStop(location)
                stop.stopId = `in`.readLong()
                stop.code = `in`.readString()
                stop.endStopCode = `in`.readString()
                stop.children = `in`.readArrayList(ScheduledStop::class.java.classLoader) as? ArrayList<ScheduledStop>
                stop.parentId = `in`.readLong()
                stop.shortName = `in`.readString()
                stop.services = `in`.readString()
                stop.currentFilter = `in`.readString()
                stop.type = from(`in`.readString()!!)
                stop.modeInfo = `in`.readParcelable(ModeInfo::class.java.classLoader)
                stop.wheelchairAccessible =
                    `in`.readValue(Boolean::class.java.classLoader) as Boolean?
                stop.bicycleAccessible = `in`.readValue(Boolean::class.java.classLoader) as Boolean?
                stop.alertHashCodes = `in`.readArrayList(Long::class.java.classLoader) as? ArrayList<Long>
                return stop
            }

            override fun newArray(size: Int): Array<ScheduledStop?> {
                return arrayOfNulls(size)
            }
        }

        @JvmStatic
        fun convertStopTypeToVehicleMode(type: StopType?): VehicleMode? {
            if (type == null) {
                return null
            }

            when (type) {
                TRAIN -> return VehicleMode.TRAIN
                BUS -> return VehicleMode.BUS
                FERRY -> return VehicleMode.FERRY
                MONORAIL -> return VehicleMode.MONORAIL
                TRAM -> return VehicleMode.TRAM
                SUBWAY -> return VehicleMode.SUBWAY
                CABLECAR -> return VehicleMode.CABLECAR
                else -> return null
            }
            return null
        }

        @DrawableRes
        fun convertStopTypeToTransportModeIcon(type: StopType?): Int {
            if (type == null) {
                return 0
            }

            return when (type) {
                TRAIN -> R.drawable.ic_train
                BUS -> R.drawable.ic_bus
                FERRY -> R.drawable.ic_ferry
                MONORAIL -> R.drawable.ic_monorail
                TRAM -> R.drawable.ic_tram
                SUBWAY -> R.drawable.ic_subway
                CABLECAR -> R.drawable.ic_cablecar
                else -> 0
            }
        }
    }
}
