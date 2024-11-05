package com.skedgo.geocoding

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sqrt


class LatLng {
    var lat: Double = 0.0
    var lng: Double = 0.0

    constructor()


    constructor(_lat: Double, _lng: Double) {
        lat = _lat
        lng = _lng
    }

    constructor(other: LatLng) {
        lat = other.lat
        lng = other.lng
    }

    /**
     * This is the Equirectangular approximation. It's a little slower than the Region.distanceInMetres()
     * formula.
     */
    fun distanceInMetres( //@NotNull
        other: LatLng
    ): Double {
        var lngDelta = abs(lng - other.lng)
        if (lngDelta > 180) lngDelta = 360 - lngDelta
        val p1 = lngDelta * cos(0.5 * radians * (lat + other.lat))
        val p2 = (lat - other.lat)
        return EarthRadius * radians * sqrt(p1 * p1 + p2 * p2)
    }

    companion object {
        const val NO_NUM: Double = -3e11
        const val EarthRadius: Double = 6371000.0
        const val radians: Double = 3.14159 / 180
        var nullLatLong: LatLng = LatLng(0.0, 0.0)
    }
}
