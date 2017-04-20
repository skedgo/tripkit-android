package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.RoutingResponse;
import skedgo.tripkit.routing.Trip;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Handles downloading trip via {@link Trip#getTemporaryURL()}.
 */
public interface TemporaryUrlApi {
  /**
   * @param url    Should be {@link Trip#getTemporaryURL()}.
   * @param config Described in <a href="https://redmine.buzzhives.com/projects/buzzhives/wiki/Main_API_formats#Default-configuration-parameters">Default configuration parameters</a>.
   */
  @GET Observable<RoutingResponse> requestTemporaryUrlAsync(
      @Url String url,
      @QueryMap Map<String, Object> config
  );
}