package skedgo.tripkit.samples.a2brouting

import android.os.Parcel
import android.os.Parcelable
import com.skedgo.tripkit.TransportModeFilter
import com.skedgo.tripkit.common.model.TransportMode

class SampleTransportModeFilter() : TransportModeFilter {
    private var transportModes: Set<String> = setOf()
    private var avoidTransportModes: Set<String> = setOf()

    fun setTransportModes(transportModes: Set<String>) {
        this.transportModes = transportModes
    }

    fun setTransportModesToAvoid(transportModesToAvoid: Set<String>) {
        this.avoidTransportModes = transportModesToAvoid
    }

    override fun useTransportMode(mode: String): Boolean {
        return when {
            transportModes.isEmpty() -> true
            else -> {
                // Wheelchair support comes with walking support.
                if (mode == TransportMode.ID_WHEEL_CHAIR && transportModes.contains(TransportMode.ID_WALK)) {
                    return true;
                } else {
                    transportModes.contains(mode)
                }
            }
        }
    }

    override fun avoidTransportMode(mode: String): Boolean {
        return when {
            avoidTransportModes.isEmpty() -> false
            else -> avoidTransportModes.contains(mode)
        }
    }

    constructor(parcel: Parcel) : this() {
        var list = mutableListOf<String>()
        parcel.readStringList(list)
        this.transportModes = list.toSet()

        var avoidList = mutableListOf<String>()
        parcel.readStringList(avoidList)
        this.avoidTransportModes = avoidList.toSet()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(transportModes.toList())
        parcel.writeStringList(avoidTransportModes.toList())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SampleTransportModeFilter> {
        override fun createFromParcel(parcel: Parcel): SampleTransportModeFilter {
            return SampleTransportModeFilter(parcel)
        }

        override fun newArray(size: Int): Array<SampleTransportModeFilter?> {
            return arrayOfNulls(size)
        }
    }
}