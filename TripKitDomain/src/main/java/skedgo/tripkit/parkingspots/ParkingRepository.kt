package skedgo.tripkit.parkingspots

import io.reactivex.Observable
import skedgo.tripkit.parkingspots.models.OffStreetParking
import skedgo.tripkit.location.GeoPoint

interface ParkingRepository {
  fun fetchCarParks(center: GeoPoint, cellIds: List<String>, zoomLevel: Int): Observable<List<OffStreetParking>>

  fun getByCellIds(ids: List<String>): Observable<List<OffStreetParking>>
}
