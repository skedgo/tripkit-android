package skedgo.tripkit.geocoding

import rx.Observable

interface Geocodable {
  fun getCoordinates(address: String): Observable<Pair<Double, Double>>
}
