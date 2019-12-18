package com.skedgo.tripkit.a2brouting;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import skedgo.tripkit.routing.RoutingResponse;

/**
 * Calculates door-to-door trips for the specified mode(s).
 * See more at https://skedgo.github.io/tripgo-api/#tag/Routing%2Fpaths%2F~1routing.json%2Fget.
 */
public interface A2bRoutingApi {
  @GET
  Observable<RoutingResponse> execute(
      @Url String url,
      @Query("modes") List<String> modes,
      @Query("avoid") List<String> excludedTransitModes,
      @Query("avoidStops") List<String> excludeStops,
      @QueryMap Map<String, Object> options
  );
}