package com.skedgo.tripkit.geocoding

import com.skedgo.tripkit.common.model.region.Region
import io.reactivex.Observable

interface Geocodable {
    fun getCoordinates(address: String, region: Region? = null): Observable<Pair<Double, Double>>
}