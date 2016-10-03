package com.skedgo.android.tripkit.routing;

import com.skedgo.android.common.model.RoutingResponse;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Calculates door-to-door trips for the specified mode(s).
 * See http://skedgo.github.io/tripgo-api/swagger/#!/Routing/get_routing_json.
 */
public interface RoutingApi {
  @GET Observable<RoutingResponse> fetchRoutesAsync(
      @Url String url,
      @Query("modes") List<String> modes,
      @Query("avoid") List<String> excludedTransitModes,
      @QueryMap Map<String, Object> options
  );
}