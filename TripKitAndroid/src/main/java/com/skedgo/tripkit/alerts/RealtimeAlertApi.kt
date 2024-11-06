package com.skedgo.tripkit.alerts

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Use [RealtimeAlertService] for easier usages.
 */
interface RealtimeAlertApi {
    /**
     * See http://skedgo.github.io/tripgo-api/swagger/#!/Transit/get_alerts_transit_json.
     *
     * @param url        e.g. https://inflationary-br-rj-riodejaneiro.tripgo.skedgo.com/satapp/alerts/transit.json.
     * The url is a composition of an URL from [Region.getURLs]
     * and `/alerts/transit.json`.
     * @param regionName Which is [Region.getName].
     */
    @GET
    fun fetchRealtimeAlertsAsync(
        @Url url: String,
        @Query("region") regionName: String
    ): Observable<RealtimeAlertResponse>
}