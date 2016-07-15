package com.skedgo.android.tripkit;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface ServiceApi {
  @GET("/service.json") Observable<ServiceResponse> getServiceAsync(
      @Query("region") String region,
      @Query("serviceTripID") String serviceTripId,
      @Query("embarkationDate") long timeInSecs,
      @Query("encode") boolean encode
  );
}