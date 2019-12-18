package skedgo.tripkit.parkingspots
import io.reactivex.Observable
import io.reactivex.Single
import skedgo.tripgo.parkingspots.models.OnStreetParkingDetails
import skedgo.tripkit.location.GeoPoint
import skedgo.tripkit.parkingspots.models.OnStreetParking

interface OnStreetParkingRepository {
  fun getByCellIds(ids: List<String>, southWest: GeoPoint, northEast: GeoPoint): Observable<List<OnStreetParking>>
  fun getParkingDetails(onStreetParking: OnStreetParking): Single<OnStreetParkingDetails>
}