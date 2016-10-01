package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.RoutingResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

interface RoutingApi {
  @Deprecated
  @GET("routing.json") RoutingResponse fetchRoutes(
      @Query("modes") List<String> modes,
      @Query("avoid") List<String> excludedTransitModes,
      @QueryMap Map<String, Object> options
  );

  @GET("routing.json") Observable<RoutingResponse> fetchRoutesAsync(
      @Query("modes") List<String> modes,
      @Query("avoid") List<String> excludedTransitModes,
      @QueryMap Map<String, Object> options
  );
}