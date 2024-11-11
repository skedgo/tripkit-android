package com.skedgo.tripkit

import com.skedgo.tripkit.routing.RoutingResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

/**
 * Handles downloading trip via [Trip.getTemporaryURL].
 */
interface TemporaryUrlApi {
    /**
     * @param url    Should be [Trip.getTemporaryURL].
     * @param config Described in [Default configuration parameters](https://redmine.buzzhives.com/projects/buzzhives/wiki/Main_API_formats#Default-configuration-parameters).
     */
    @GET
    fun requestTemporaryUrlAsync(
        @Url url: String?,
        @QueryMap config: Map<String, Any>
    ): Observable<RoutingResponse>
}