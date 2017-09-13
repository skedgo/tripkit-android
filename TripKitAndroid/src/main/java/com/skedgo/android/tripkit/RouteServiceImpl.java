package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.tripkit.routing.ExtraQueryMapProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import skedgo.tripkit.a2brouting.FailoverA2bRoutingApi;
import skedgo.tripkit.a2brouting.RouteService;
import skedgo.tripkit.a2brouting.ToWeightingProfileString;
import skedgo.tripkit.routing.TripGroup;

final class RouteServiceImpl implements RouteService {
  private final Func1<Query, Observable<List<Query>>> queryGenerator;
  private final ExcludedTransitModesAdapter excludedTransitModesAdapter;
  @Nullable private final Co2Preferences co2Preferences;
  @Nullable private final TripPreferences tripPreferences;
  @Nullable private final ExtraQueryMapProvider extraQueryMapProvider;
  private final FailoverA2bRoutingApi routingApi;

  RouteServiceImpl(
      @NonNull Func1<Query, Observable<List<Query>>> queryGenerator,
      @Nullable ExcludedTransitModesAdapter excludedTransitModesAdapter,
      @Nullable Co2Preferences co2Preferences,
      @Nullable TripPreferences tripPreferences,
      @Nullable ExtraQueryMapProvider extraQueryMapProvider,
      FailoverA2bRoutingApi routingApi) {
    this.queryGenerator = queryGenerator;
    this.excludedTransitModesAdapter = excludedTransitModesAdapter;
    this.co2Preferences = co2Preferences;
    this.tripPreferences = tripPreferences;
    this.extraQueryMapProvider = extraQueryMapProvider;
    this.routingApi = routingApi;
  }

  private static String toCoordinatesText(Location location) {
    StringBuilder coordinatesText = new StringBuilder("(" + location.getLat() + "," + location.getLon() + ")");
    if (!TextUtils.isEmpty(location.getAddress())) {
      coordinatesText.append("\"").append(location.getAddress()).append("\"");
    }
    return coordinatesText.toString();
  }

  @NonNull @Override
  public Observable<List<TripGroup>> routeAsync(@NonNull Query query) {
    return flatSubQueries(query)
        .flatMap(new Func1<Query, Observable<List<TripGroup>>>() {
          @Override public Observable<List<TripGroup>> call(Query subQuery) {
            final Region region = subQuery.getRegion();
            final List<String> baseUrls = region.getURLs();
            final List<String> modes = subQuery.getTransportModeIds();
            final List<String> excludedTransitModes = getExcludedTransitModesAsNonNull(
                excludedTransitModesAdapter,
                region.getName()
            );
            final Map<String, Object> options = toOptions(subQuery);
            return routingApi.fetchRoutesAsync(baseUrls, modes, excludedTransitModes, options);
          }
        });
  }

  @NonNull List<String> getExcludedTransitModesAsNonNull(
      @Nullable ExcludedTransitModesAdapter excludedTransitModesAdapter,
      String regionName) {
    if (excludedTransitModesAdapter != null) {
      final List<String> modes = excludedTransitModesAdapter.call(regionName);
      return modes == null ? Collections.<String>emptyList() : modes;
    } else {
      return Collections.emptyList();
    }
  }

  /* TODO: Consider making this public for Xerox team. */
  @NonNull Map<String, Object> getParamsByPreferences() {
    final ArrayMap<String, Object> map = new ArrayMap<>();
    if (tripPreferences != null) {
      if (tripPreferences.isConcessionPricingPreferred()) {
        map.put("conc", true);
      }
      if (tripPreferences.isWheelchairPreferred()) {
        map.put("wheelchair", true);
      }
    }

    if (co2Preferences != null) {
      final Map<String, Float> co2Profile = co2Preferences.getCo2Profile();
      for (Map.Entry<String, Float> entry : co2Profile.entrySet()) {
        map.put("co2[" + entry.getKey() + "]", entry.getValue());
      }
    }

    return map;
  }

  @NonNull Map<String, Object> toOptions(@NonNull Query query) {
    final String departureCoordinates = toCoordinatesText(query.getFromLocation());
    final String arrivalCoordinates = toCoordinatesText(query.getToLocation());
    final long arriveBefore = TimeUnit.MILLISECONDS.toSeconds(query.getArriveBy());
    final long departAfter = TimeUnit.MILLISECONDS.toSeconds(query.getDepartAfter());
    final String unit = query.getUnit();
    final int transferTime = query.getTransferTime();
    final int walkingSpeed = query.getWalkingSpeed();
    final int cyclingSpeed = query.getCyclingSpeed();

    final Map<String, Object> options = new HashMap<>();
    options.put("from", departureCoordinates);
    options.put("to", arrivalCoordinates);
    options.put("arriveBefore", Long.toString(arriveBefore));
    options.put("departAfter", Long.toString(departAfter));
    options.put("unit", unit);
    options.put("v", "12");
    options.put("tt", Integer.toString(transferTime));
    options.put("ws", Integer.toString(walkingSpeed));
    options.put("cs", Integer.toString(cyclingSpeed));
    options.put("includeStops", "1");
    // FIXME: wheelchair option should be retrieved from settings (Vinh is working on that)
    options.put("wheelchair", "1");
    options.put("wp", ToWeightingProfileString.INSTANCE.toWeightingProfileString(query));
    options.putAll(getParamsByPreferences());
    if (extraQueryMapProvider != null) {
      final Map<String, Object> extraQueryMap = extraQueryMapProvider.call();
      options.putAll(extraQueryMap);
    }
    List<String> excludeStops = query.getExcludedStopCodes();
    if (excludeStops != null) {
      for (String stop : excludeStops) {
        options.put("avoidStops", stop);
      }
    }
    return options;
  }

  private Observable<Query> flatSubQueries(@NonNull Query query) {
    return queryGenerator.call(query)
        .flatMap(new Func1<List<Query>, Observable<Query>>() {
          @Override
          public Observable<Query> call(List<Query> queries) {
            return Observable.from(queries);
          }
        });
  }
}