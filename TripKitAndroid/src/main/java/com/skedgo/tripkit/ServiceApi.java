package com.skedgo.tripkit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Observable;

public interface ServiceApi {
  @GET("service.json") Observable<ServiceResponse> getServiceAsync(
      @Query("region") String region,
      @Query("serviceTripID") String serviceTripId,
      @Query("embarkationDate") long timeInSecs,
      @Query("encode") boolean encode
  );
}