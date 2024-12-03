package com.skedgo.tripkit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface LocationInfoApi {
    @GET
    fun fetchLocationInfoAsync(
        @Url url: String,
        @Query("lat") lat: Double?,
        @Query("lng") lng: Double?
    ): Observable<LocationInfo>
}