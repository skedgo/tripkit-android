package com.skedgo.tripkit.parkingspots

import com.skedgo.tripkit.location.GeoPoint
import com.skedgo.tripkit.parkingspots.models.OnStreetParking
import io.reactivex.Observable
import io.reactivex.Single
import skedgo.tripgo.parkingspots.models.OnStreetParkingDetails

interface OnStreetParkingRepository {
    fun getByCellIds(
        ids: List<String>,
        southWest: GeoPoint,
        northEast: GeoPoint
    ): Observable<List<OnStreetParking>>

    fun getParkingDetails(onStreetParking: OnStreetParking): Single<OnStreetParkingDetails>
}