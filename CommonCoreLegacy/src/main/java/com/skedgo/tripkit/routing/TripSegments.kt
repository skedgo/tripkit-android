package com.skedgo.tripkit.routing

object TripSegments {
    fun getTransportColor(segment: TripSegment?): ServiceColor? {
        if (segment == null) {
            return null
        } else {
            val color = segment.serviceColor
            if (color != null) {
                return color
            } else {
                val modeInfo = segment.modeInfo
                return modeInfo?.color
            }
        }
    }
}