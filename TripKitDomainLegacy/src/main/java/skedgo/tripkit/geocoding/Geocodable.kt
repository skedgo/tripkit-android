package skedgo.tripkit.geocoding

import com.skedgo.android.common.model.Region
import io.reactivex.Observable

interface Geocodable {
  fun getCoordinates(address: String, region: Region? = null): Observable<Pair<Double, Double>>
}