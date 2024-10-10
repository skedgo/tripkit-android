package com.skedgo.tripkit.common.model.region

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.skedgo.tripkit.common.model.TransportMode

object Regions {
    @JvmStatic
    fun equals(a: Region?, b: Region?): Boolean {
        return a == b
    }

    @JvmStatic
    fun createInterRegion(
        departureRegion: Region,
        arrivalRegion: Region
    ): Region {
        return InterRegion(departureRegion, arrivalRegion)
    }

    class InterRegion(departureRegion: Region, arrivalRegion: Region) : Region() {
        init {
            name = departureRegion.name + "_" + arrivalRegion.name

            val departureModeIds = departureRegion.transportModeIds
            val arrivalModeIds = arrivalRegion.transportModeIds
            if (departureModeIds != null && arrivalModeIds != null) {
                val unionModeIds: MutableSet<String> = LinkedHashSet(
                    departureModeIds.size + arrivalModeIds.size + 1
                )
                unionModeIds.add(TransportMode.ID_AIR)
                unionModeIds.addAll(departureModeIds)
                unionModeIds.addAll(arrivalModeIds)
                transportModeIds = ArrayList(unionModeIds)
            } else if (departureModeIds != null) {
                val unionModeIds: MutableSet<String> = LinkedHashSet(
                    departureModeIds.size + 1
                )
                unionModeIds.add(TransportMode.ID_AIR)
                unionModeIds.addAll(departureModeIds)
                transportModeIds = ArrayList(unionModeIds)
            } else if (arrivalModeIds != null) {
                val unionModeIds: MutableSet<String> = LinkedHashSet(
                    arrivalModeIds.size + 1
                )
                unionModeIds.add(TransportMode.ID_AIR)
                unionModeIds.addAll(arrivalModeIds)
                transportModeIds = ArrayList(unionModeIds)
            }

            val departureUrls = departureRegion.getURLs()
            if (departureUrls != null) {
                setURLs(ArrayList(departureUrls))
            }

            timezone = departureRegion.timezone
            encodedPolyline = departureRegion.encodedPolyline
        }
    }
}