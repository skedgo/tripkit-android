package com.skedgo.tripkit.common.model.region

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.SerializedName
import com.skedgo.tripkit.common.model.TransportMode

class RegionsResponse {
    @JvmField
    @set:VisibleForTesting
    @SerializedName("regions")
    var regions: ArrayList<Region>? = null

    @SerializedName("modes")
    private var transportModeMap: Map<String, TransportMode>? = null

    val transportModes: Collection<TransportMode>?
        get() {
            correctModeIds()
            return if (transportModeMap != null
            ) transportModeMap?.values
            else null
        }

    @VisibleForTesting
    fun setTransportModeMap(transportModeMap: Map<String, TransportMode>?) {
        this.transportModeMap = transportModeMap
    }

    private fun correctModeIds() {
        if (transportModeMap != null) {
            for ((key, value) in transportModeMap!!) {
                if (value != null) {
                    value.id = key
                }
            }
        }
    }
}