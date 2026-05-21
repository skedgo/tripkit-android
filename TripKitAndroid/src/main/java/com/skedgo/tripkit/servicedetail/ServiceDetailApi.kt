package com.skedgo.tripkit.servicedetail

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ServiceDetailApi {
    @GET
    fun getServiceAsync(
        @Url url: String,
        @Query("region") region: String,
        @Query("serviceTripID") serviceTripId: String,
        @Query("operator") operator: String?,
        @Query("startStopCode") startStopCode: String?,
        @Query("endStopCode") endStopCode: String?,
        @Query("embarkationDate") timeInSecs: Long,
        @Query("encode") encode: Boolean
    ): Observable<JsonObject>
}

