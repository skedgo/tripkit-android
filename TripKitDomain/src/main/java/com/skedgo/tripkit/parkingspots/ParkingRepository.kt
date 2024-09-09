package com.skedgo.tripkit.parkingspots

import com.skedgo.tripkit.location.GeoPoint
import com.skedgo.tripkit.parkingspots.models.OffStreetParking
import io.reactivex.Observable

interface ParkingRepository {
    fun fetchCarParks(
        center: GeoPoint,
        cellIds: List<String>,
        zoomLevel: Int
    ): Observable<List<OffStreetParking>>

    fun getByCellIds(ids: List<String>): Observable<List<OffStreetParking>>
}
