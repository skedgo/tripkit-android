package skedgo.tripkit.geocoding

import io.reactivex.Observable

interface ReverseGeocodable {
  fun getAddress(latitude: Double, longitude: Double): Observable<String>
}
