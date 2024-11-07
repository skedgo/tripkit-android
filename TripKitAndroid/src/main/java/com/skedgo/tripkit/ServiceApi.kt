package com.skedgo.tripkit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {
    @GET("service.json")
    fun getServiceAsync(
        @Query("region") region: String,
        @Query("serviceTripID") serviceTripId: String,
        @Query("operator") operator: String?,
        @Query("startStopCode") startStopCode: String,
        @Query("endStopCode") endStopCode: String?,
        @Query("embarkationDate") timeInSecs: Long,
        @Query("encode") encode: Boolean
    ): Observable<ServiceResponse>
}