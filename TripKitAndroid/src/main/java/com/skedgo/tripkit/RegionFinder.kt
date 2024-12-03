package com.skedgo.tripkit

import com.skedgo.tripkit.common.model.region.Region
import com.skedgo.tripkit.common.util.PolyUtil
import com.skedgo.tripkit.common.util.TripKitLatLng
import java.util.concurrent.ConcurrentHashMap

class RegionFinder {

    private val polygonCache: MutableMap<String, List<TripKitLatLng>> = ConcurrentHashMap()

    fun contains(region: Region, lat: Double, lng: Double): Boolean {
        val polygon = getPolygon(region, polygonCache)
        return polygon != null && PolyUtil.containsLocation(lat, lng, polygon, true)
    }

    fun invalidate() {
        polygonCache.clear()
    }

    private fun getPolygon(
        region: Region,
        polygonCache: MutableMap<String, List<TripKitLatLng>>
    ): List<TripKitLatLng>? {
        val name = region.name.orEmpty()
        var polygon = polygonCache[name]
        if (polygon == null) {
            polygon = PolyUtil.decode(region.encodedPolyline)
            polygonCache[name] = polygon
        }
        return polygon
    }
}
