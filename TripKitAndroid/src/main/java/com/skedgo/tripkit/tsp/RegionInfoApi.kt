package com.skedgo.tripkit.tsp

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Retrieves detailed information about covered
 * transport service providers for the specified regions.
 *
 *
 * See http://skedgo.github.io/tripgo-api/swagger/#!/Configuration/post_regionInfo_json.
 * See [RegionInfoService] for easier usage.
 */
interface RegionInfoApi {
    @POST("regionInfo.json")
    fun fetchRegionInfo(
        @Body body: RegionInfoBody
    ): RegionInfoResponse

    /**
     * @param url The url is a composition of an URL
     * from [Region.getURLs] and 'regionInfo.json'.
     */
    @POST
    fun fetchRegionInfoAsync(
        @Url url: String,
        @Body body: RegionInfoBody
    ): Observable<RegionInfoResponse>
}