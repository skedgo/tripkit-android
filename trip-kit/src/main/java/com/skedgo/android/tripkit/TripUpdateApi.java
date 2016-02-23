package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.RoutingResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

interface TripUpdateApi {
  @GET Observable<RoutingResponse> fetchUpdateAsync(
      @Url String url,
      @Query("v") String apiVersion
  );
}