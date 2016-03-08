package com.skedgo.android.tripkit;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.RoutingResponse;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.util.Gsons;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

final class RouteServiceImpl implements RouteService {
  private final Func1<TripGroup, TripGroup> selectBestDisplayTrip = new SelectBestDisplayTrip();
  private final Func1<List<TripGroup>, List<TripGroup>> fillIdentifiers = new FillIdentifiers();
  private final String appVersion;
  private final Func1<Query, Observable<List<Query>>> queryGenerator;
  private final Resources resources;
  private final Func1<String, RoutingApi> routingApiFactory;
  private final Func1<String, List<String>> avoidModesLoader;

  RouteServiceImpl(
      @NonNull Resources resources,
      @NonNull String appVersion,
      @NonNull Func1<Query, Observable<List<Query>>> queryGenerator,
      Func1<String, RoutingApi> routingApiFactory,
      @NonNull Func1<String, List<String>> avoidModesLoader) {
    this.appVersion = appVersion;
    this.queryGenerator = queryGenerator;
    this.resources = resources;
    this.routingApiFactory = routingApiFactory;
    this.avoidModesLoader = avoidModesLoader;
  }

  private static String toCoordinatesText(Location location) {
    return "(" + location.getLat() + "," + location.getLon() + ")";
  }

  @NonNull @Override
  public Observable<TripGroup> routeAsync(@NonNull RouteOptions options) {
    final Query query = options.toQuery();
    return routeAsync(query)
        .flatMap(new Func1<List<TripGroup>, Observable<TripGroup>>() {
          @Override
          public Observable<TripGroup> call(List<TripGroup> routes) {
            return Observable.from(routes);
          }
        });
  }

  @NonNull @Override
  public Observable<List<TripGroup>> routeAsync(@NonNull Query query) {
    return flatSubQueries(query)
        .flatMap(new Func1<Query, Observable<List<TripGroup>>>() {
          @Override
          public Observable<List<TripGroup>> call(Query subQuery) {
            final List<String> urls = subQuery.getRegion().getURLs();
            final List<String> modes = subQuery.getTransportModeIds();
            final Map<String, String> options = toOptions(subQuery);
            return fetchRoutesAsync(urls, modes, avoidModesLoader.call(subQuery.getRegion().getName()), options);
          }
        });
  }

  @NonNull Map<String, String> toOptions(@NonNull Query query) {
    final String departureCoordinates = toCoordinatesText(query.getFromLocation());
    final String arrivalCoordinates = toCoordinatesText(query.getToLocation());
    final long arriveBefore = TimeUnit.MILLISECONDS.toSeconds(query.getArriveBy());
    final long departAfter = TimeUnit.MILLISECONDS.toSeconds(query.getDepartAfter());
    final String unit = query.getUnit();
    final int transferTime = query.getTransferTime();
    final int walkingSpeed = query.getWalkingSpeed();

    final Map<String, String> options = new HashMap<>();
    options.put("from", departureCoordinates);
    options.put("to", arrivalCoordinates);
    options.put("arriveBefore", Long.toString(arriveBefore));
    options.put("departAfter", Long.toString(departAfter));
    options.put("unit", unit);
    options.put("version", appVersion);
    options.put("v", "11");
    options.put("tt", Integer.toString(transferTime));
    options.put("ws", Integer.toString(walkingSpeed));
    if (query.isInterRegional()) {
      options.put("ir", "1");
    }
    return options;
  }

  @NonNull Observable<Query> flatSubQueries(@NonNull Query query) {
    return queryGenerator.call(query)
        .flatMap(new Func1<List<Query>, Observable<Query>>() {
          @Override
          public Observable<Query> call(List<Query> queries) {
            return Observable.from(queries);
          }
        });
  }

  /**
   * Regarding url failover, see more: https://www.flowdock.com/app/skedgo/tripgo-v4/threads/ZSuBe4bGCfR8ltaEosihtCklBZy.
   */
  @NonNull Observable<List<TripGroup>> fetchRoutesAsync(
      @NonNull List<String> urls,
      @NonNull final List<String> modes,
      @NonNull final List<String> avoidModeIds,
      final Map<String, String> options) {
    // TODO: Write tests to assert the logic of url failover.
    return Observable.from(urls)
        .concatMap(new Func1<String, Observable<RoutingResponse>>() {
          @Override public Observable<RoutingResponse> call(final String url) {
            return fetchRoutesPerUrlAsync(url, modes, avoidModeIds, options);
          }
        })
        .first()
        .map(new Func1<RoutingResponse, List<TripGroup>>() {
          @Override public List<TripGroup> call(RoutingResponse response) {
            response.processRawData(resources, Gsons.createForLowercaseEnum());
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
        })
        .subscribeOn(Schedulers.io());
  }

  /**
   * If the {@link Observable} returned is empty, {@link RouteServiceImpl#fetchRoutesAsync(List, List, List, Map)}
   * will failover on a different url.
   */
  @NonNull Observable<RoutingResponse> fetchRoutesPerUrlAsync(
      final String url,
      @NonNull final List<String> modes,
      @NonNull final List<String> avoidModes,
      final Map<String, String> options) {
    return Observable
        .create(new Observable.OnSubscribe<RoutingResponse>() {
          @Override public void call(Subscriber<? super RoutingResponse> subscriber) {
            try {
              final RoutingApi api = routingApiFactory.call(url);
              final RoutingResponse response = api.fetchRoutes(modes, avoidModes, options);
              subscriber.onNext(response);
              subscriber.onCompleted();
            } catch (Exception e) {
              subscriber.onError(e);
            }
          }
        })
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