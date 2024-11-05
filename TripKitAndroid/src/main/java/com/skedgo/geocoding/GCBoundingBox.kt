package com.skedgo.geocoding

import com.skedgo.geocoding.agregator.GCBoundingBoxInterface
import kotlin.math.cos


class GCBoundingBox : GCBoundingBoxInterface {
    override var latN: Double = 0.0
    override var latS: Double = 0.0
    override var lngW: Double = 0.0
    override var lngE: Double = 0.0
    var latitudeDelta: Double = -1.0
        get() {
            if (field == -1.0) {
                val latitudeSpan = latS - latN
                field = latitudeSpan * Math.PI / 180
            }
            return field
        }
        private set
    var longitudeDelta: Double = -1.0
        get() {
            if (field == -1.0) {
                field = cos(latitudeDelta) * Math.PI / 180
            }
            return field
        }
        private set

    constructor(lat1: Double, lat2: Double, lng1: Double, lng2: Double) {
        if (lat1 < lat2) {
            this.latN = lat1
            this.latS = lat2
        } else {
            this.latN = lat2
            this.latS = lat1
        }
        if (lng1 < lng2) {
            this.lngW = lng1
            this.lngE = lng2
        } else {
            this.lngW = lng2
            this.lngE = lng1
        }
    }


    constructor(bb: GCBoundingBoxInterface) {
        if (bb.latN < bb.latS) {
            this.latN = bb.latN
            this.latS = bb.latS
        } else {
            this.latN = bb.latS
            this.latS = bb.latN
        }
        if (bb.lngW < bb.lngE) {
            this.lngW = bb.lngW
            this.lngE = bb.lngE
        } else {
            this.lngW = bb.lngE
            this.lngE = bb.lngW
        }
    }

    constructor( //@NotNull
        other: GCBoundingBox
    ) {
        latN = other.latN
        latS = other.latS
        lngW = other.lngW
        lngE = other.lngE
    }

    fun height(): Int {
        // This constant is valid for all locations on Earth, since lines of latitude are equally spaced.
        return ((latS - latN) * 110852).toInt()
    }

    fun center(): LatLng {
        return LatLng(((latS - latN) / 2) + this.latN, ((lngE - lngW) / 2) + this.lngW)
    }

    val boundingBox: GCBoundingBox
        get() = this

    val latLngs: List<LatLng>
        get() {
            val result: MutableList<LatLng> = ArrayList()
            result.add(LatLng(latN, lngW))
            result.add(LatLng(latN, lngE))
            result.add(LatLng(latS, lngE))
            result.add(LatLng(latS, lngW))
            return result
        }

    companion object {
        val World: GCBoundingBox = GCBoundingBox(85.0, -85.0, -180.0, 180.0)
    }
}
