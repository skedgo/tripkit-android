package com.skedgo.tripkit.servicedetail

import com.skedgo.tripkit.ServiceResponse
import io.reactivex.Observable

interface ServiceDetailRepository {
    /**
     * Uses the provided [baseUrls] (region servers) to fetch the service.
     *
     * This avoids re-fetching region details when the caller already has them.
     */
    fun getService(
        baseUrls: List<String>,
        region: String,
        serviceTripId: String,
        operator: String?,
        startStopCode: String,
        endStopCode: String?,
        embarkationTimeInSecs: Long,
        encode: Boolean = true
    ): Observable<ServiceResponse>

    fun getService(
        region: String,
        serviceTripId: String,
        operator: String?,
        startStopCode: String,
        endStopCode: String?,
        embarkationTimeInSecs: Long,
        encode: Boolean = true
    ): Observable<ServiceResponse>
}

