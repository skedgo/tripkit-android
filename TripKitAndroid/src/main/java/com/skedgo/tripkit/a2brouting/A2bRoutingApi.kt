package com.skedgo.tripkit.a2brouting

import com.skedgo.tripkit.routing.RoutingResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Calculates door-to-door trips for the specified mode(s).
 * See more at https://skedgo.github.io/tripgo-api/#tag/Routing%2Fpaths%2F~1routing.json%2Fget.
 */
interface A2bRoutingApi {
    @GET
    fun execute(
        @Url url: String,
        @Query("modes") modes: List<String>,
        @Query("avoid") excludedTransitModes: List<String>,
        @Query("avoidStops") excludeStops: List<String>,
        @QueryMap options: Map<String, @JvmSuppressWildcards Any>
    ): Observable<RoutingResponse>
}