package com.skedgo.tripkit

import com.skedgo.tripkit.common.model.location.Location
import io.reactivex.Observable

interface LocationInfoService {
    fun getLocationInfoAsync(location: Location?): Observable<LocationInfo>
}