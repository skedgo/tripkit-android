package com.skedgo.tripkit.a2brouting;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.skedgo.tripkit.common.model.Region;
import com.skedgo.tripkit.RoutingUserError;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import com.skedgo.tripkit.routing.RoutingResponse;
import com.skedgo.tripkit.routing.TripGroup;

/**
 * A wrapper of {@link A2bRoutingApi} that requests `routing.json`
 * on multiple servers w/ failover.
 */
public class FailoverA2bRoutingApi {
  private final SelectBestDisplayTrip selectBestDisplayTrip = new SelectBestDisplayTrip();
  private final FillIdentifiers fillIdentifiers = new FillIdentifiers();
  private final Resources resources;
  private final Gson gson;
  private final A2bRoutingApi a2bRoutingApi;

  public FailoverA2bRoutingApi(
      Resources resources,
      Gson gson,
      A2bRoutingApi a2bRoutingApi
  ) {
    this.resources = resources;
    this.gson = gson;
    this.a2bRoutingApi = a2bRoutingApi;
  }

  /**
   * Fetches routes on multiple base urls serially.
   * If it fails on one url, it'll failover on next url.
   *
   * @param baseUrls Can be obtained by {@link Region#getURLs()}.
   */
  public Observable<List<TripGroup>> fetchRoutesAsync(
      List<String> baseUrls,
      final List<String> modes,
      final List<String> excludedTransitModes,
      final List<String> excludeStops,
      final Map<String, Object> options
  ) {
    // Regarding url failover, see more
    // https://www.flowdock.com/app/skedgo/tripgo-v4/threads/ZSuBe4bGCfR8ltaEosihtCklBZy.
    return Observable.fromIterable(baseUrls)
        .map(baseUrl -> HttpUrl.parse(baseUrl).newBuilder()
            .addPathSegment("routing.json")
            .build()
            .toString())
        .concatMap((Function<String, Observable<RoutingResponse>>) url -> fetchRoutesPerUrlAsync(url, modes, excludedTransitModes, excludeStops, options))
        .first(new RoutingResponse())
        .map((Function<RoutingResponse, List<TripGroup>>) response -> {
          response.processRawData(resources, gson);
          return response.getTripGroupList();
        })
        .filter(CollectionUtils::isNotEmpty)
        .map(fillIdentifiers)
        .map(groups -> {
            for (TripGroup group : groups) {
            selectBestDisplayTrip.apply(group);
          }
          return groups;
        })
        .onErrorResumeNext((Function<Throwable, Maybe<? extends List<TripGroup>>>) error -> error instanceof RoutingUserError
            ? Maybe.error(error)
            : Maybe.empty()).toObservable();
  }

  Observable<RoutingResponse> fetchRoutesPerUrlAsync(
      final String url,
      final List<String> modes,
      final List<String> excludedTransitModes,
      final List<String> excludeStops,
      final Map<String, Object> options
  ) {
    return a2bRoutingApi
        .execute(url, modes, excludedTransitModes, excludeStops, options)
        .filter(response -> !(response.getErrorMessage() != null && !response.hasError()))
        /* Let it fail silently. */
        .onErrorResumeNext(Observable.empty())
        .flatMap((Function<RoutingResponse, Observable<RoutingResponse>>) response -> {
          if (response.getErrorMessage() != null) {
            return Observable.error(new RoutingUserError(response.getErrorMessage()));
          } else {
            return Observable.just(response);
          }
        });
  }
}