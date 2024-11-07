package com.skedgo.tripkit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceApi {
    @GET("service.json")
    Observable<ServiceResponse> getServiceAsync(
        @Query("region") String region,
        @Query("serviceTripID") String serviceTripId,
        @Query("operator") String operator,
        @Query("startStopCode") String startStopCode,
        @Query("endStopCode") String endStopCode,
        @Query("embarkationDate") long timeInSecs,
        @Query("encode") boolean encode
    );
}