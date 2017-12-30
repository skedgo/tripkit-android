package skedgo.tripkit.geocoding

import rx.Observable

interface ReverseGeocodable {
  fun getAddress(latitude: Double, longitude: Double): Observable<String>
}
