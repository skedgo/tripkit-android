package com.skedgo.android.tripkit;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ServiceApi {
  @GET("/service.json") Observable<TransitService> getServiceAsync(
      @Query("region") String region,
      @Query("serviceTripID") String serviceTripId,
      @Query("embarkationDate") long timeInSecs,
      @Query("encode") boolean encode
  );
}