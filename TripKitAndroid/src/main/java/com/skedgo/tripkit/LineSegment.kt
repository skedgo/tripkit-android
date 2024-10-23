package com.skedgo.tripkit

import com.skedgo.tripkit.common.util.TripKitLatLng

/**
 * Represents a segment of a polyline denoting a [Trip].
 */
class LineSegment(
    val start: TripKitLatLng,
    val end: TripKitLatLng,
    val color: Int,
    val tag: String,
    val tripUuid: String? = null
) {
    enum class Tag {
        SHAPE,
        STREET
    }
}