package skedgo.tripkit.geocoding

import com.skedgo.android.common.model.Region
import rx.Observable

interface GeoCodable {
  fun getCoordinates(address: String, region: Region? = null): Observable<Pair<Double, Double>>
}