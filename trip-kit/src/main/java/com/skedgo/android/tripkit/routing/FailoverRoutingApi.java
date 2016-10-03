package com.skedgo.android.tripkit.routing;

import android.content.res.Resources;

import com.google.gson.Gson;
import com.skedgo.android.common.model.RoutingResponse;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.tripkit.RoutingUserError;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import rx.Observable;
import rx.functions.Func1;

public class FailoverRoutingApi {
  private final SelectBestDisplayTrip selectBestDisplayTrip = new SelectBestDisplayTrip();
  private final FillIdentifiers fillIdentifiers = new FillIdentifiers();
  private final Resources resources;
  private final Gson gson;
  private final RoutingApi routingApi;

  public FailoverRoutingApi(
      Resources resources,
      Gson gson,
      RoutingApi routingApi
  ) {
    this.resources = resources;
    this.gson = gson;
    this.routingApi = routingApi;
  }

  public Observable<List<TripGroup>> fetchRoutesAsync(
      List<String> baseUrls,
      final List<String> modes,
      final List<String> excludedTransitModes,
      final Map<String, Object> options
  ) {
    // Regarding url failover, see more
    // https://www.flowdock.com/app/skedgo/tripgo-v4/threads/ZSuBe4bGCfR8ltaEosihtCklBZy.
    return Observable.from(baseUrls)
        .map(new Func1<String, String>() {
          @Override public String call(String baseUrl) {
            return HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("routing.json")
                .build()
                .toString();
          }
        })
        .concatMap(new Func1<String, Observable<RoutingResponse>>() {
          @Override public Observable<RoutingResponse> call(String url) {
            return fetchRoutesPerUrlAsync(url, modes, excludedTransitModes, options);
          }
        })
        .first()
        .map(new Func1<RoutingResponse, List<TripGroup>>() {
          @Override public List<TripGroup> call(RoutingResponse response) {
            response.processRawData(resources, gson);
            return response.getTripGroupList();
          }
        })
        .filter(new Func1<List<TripGroup>, Boolean>() {
          @Override public Boolean call(List<TripGroup> groups) {
            return CollectionUtils.isNotEmpty(groups);
          }
        })
        .map(fillIdentifiers)
        .map(new Func1<List<TripGroup>, List<TripGroup>>() {
          @Override public List<TripGroup> call(List<TripGroup> groups) {
            for (TripGroup group : groups) {
              selectBestDisplayTrip.call(group);
            }
            return groups;
          }
        })
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<TripGroup>>>() {
          @Override public Observable<? extends List<TripGroup>> call(Throwable error) {
            return error instanceof RoutingUserError
                ? Observable.<List<TripGroup>>error(error)
                : Observable.<List<TripGroup>>empty();
          }
        });
  }

  Observable<RoutingResponse> fetchRoutesPerUrlAsync(
      final String url,
      final List<String> modes,
      final List<String> excludedTransitModes,
      final Map<String, Object> options
  ) {
    return routingApi
        .fetchRoutesAsync(url, modes, excludedTransitModes, options)
        .filter(new Func1<RoutingResponse, Boolean>() {
          @Override public Boolean call(RoutingResponse response) {
            return !(response.getErrorMessage() != null && !response.hasError());
          }
        })
        /* Let it fail silently. */
        .onErrorResumeNext(Observable.<RoutingResponse>empty())
        .flatMap(new Func1<RoutingResponse, Observable<RoutingResponse>>() {
          @Override public Observable<RoutingResponse> call(RoutingResponse response) {
            if (response.getErrorMessage() != null) {
              return Observable.error(new RoutingUserError(response.getErrorMessage()));
            } else {
              return Observable.just(response);
            }
          }
        });
  }
}