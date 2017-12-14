package skedgo.tripkit.geocoding

import rx.Observable

interface Geocodable {
  fun getAddress(latitude: Double, longitude: Double): Observable<String>
}

interface ReverseGeoCodable {
  fun getCoordinates(address: String): Observable<Pair<Double, Double>>
}