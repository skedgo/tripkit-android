package skedgo.tripkit.geocoding

import rx.Observable

interface GeoCodable {
  fun getCoordinates(address: String): Observable<Pair<Double, Double>>
}