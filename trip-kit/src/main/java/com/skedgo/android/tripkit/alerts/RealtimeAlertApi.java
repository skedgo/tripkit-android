package com.skedgo.android.tripkit.alerts;

import com.skedgo.android.common.model.Region;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Use {@link RealtimeAlertService} for easier usages.
 */
public interface RealtimeAlertApi {
  /**
   * See http://skedgo.github.io/tripgo-api/swagger/#!/Transit/get_alerts_transit_json.
   *
   * @param url        e.g. https://inflationary-br-rj-riodejaneiro.tripgo.skedgo.com/satapp/alerts/transit.json.
   *                   The url is a composition of an URL from {@link Region#getURLs()}
   *                   and `/alerts/transit.json`.
   * @param regionName Which is {@link Region#getName()}.
   */
  @GET Observable<RealtimeAlertResponse> fetchRealtimeAlertsAsync(
      @Url String url,
      @Query("region") String regionName
  );
}