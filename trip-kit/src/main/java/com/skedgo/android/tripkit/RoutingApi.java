package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.RoutingResponse;

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;

interface RoutingApi {
  @GET("/routing.json") RoutingResponse fetchRoutes(
      @Query("modes") List<String> modes,
      @QueryMap Map<String, Object> options
  );
}