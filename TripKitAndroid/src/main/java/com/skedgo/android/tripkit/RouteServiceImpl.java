package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Query;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.tripkit.routing.ExtraQueryMapProvider;
import com.skedgo.android.tripkit.tsp.RegionInfo;
import com.skedgo.android.tripkit.tsp.RegionInfoRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kotlin.Pair;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import skedgo.tripkit.a2brouting.FailoverA2bRoutingApi;
import skedgo.tripkit.a2brouting.RouteService;
import skedgo.tripkit.a2brouting.ToWeightingProfileString;
import skedgo.tripkit.routing.TripGroup;

final class RouteServiceImpl implements RouteService {
  private final Func2<Query, ModeFilter, Observable<List<Query>>> queryGenerator;
  private final ExcludedTransitModesAdapter excludedTransitModesAdapter;
  @Nullable private final Co2Preferences co2Preferences;
  @Nullable private final TripPreferences tripPreferences;
  @Nullable private final ExtraQueryMapProvider extraQueryMapProvider;
  private final FailoverA2bRoutingApi routingApi;
  private final RegionInfoRepository regionInfoRepository;

  RouteServiceImpl(
      @NonNull Func2<Query, ModeFilter, Observable<List<Query>>> queryGenerator,
      @Nullable ExcludedTransitModesAdapter excludedTransitModesAdapter,
      @Nullable Co2Preferences co2Preferences,
      @Nullable TripPreferences tripPreferences,
      @Nullable ExtraQueryMapProvider extraQueryMapProvider,
      FailoverA2bRoutingApi routingApi,
      RegionInfoRepository regionInfoRepository) {
    this.queryGenerator = queryGenerator;
    this.excludedTransitModesAdapter = excludedTransitModesAdapter;
    this.co2Preferences = co2Preferences;
    this.tripPreferences = tripPreferences;
    this.extraQueryMapProvider = extraQueryMapProvider;
    this.routingApi = routingApi;
    this.regionInfoRepository = regionInfoRepository;
  }

  private static String toCoordinatesText(Location location) {
    StringBuilder coordinatesText = new StringBuilder("(" + location.getLat() + "," + location.getLon() + ")");
    if (!TextUtils.isEmpty(location.getAddress())) {
      coordinatesText.append("\"").append(location.getAddress()).append("\"");
    }
    return coordinatesText.toString();
  }

  @NonNull @Override
  public Observable<List<TripGroup>> routeAsync(@NonNull Query query, @NonNull ModeFilter modeFilter) {
    return flatSubQueries(query, modeFilter)
        .concatMap(subQuery ->
                       regionInfoRepository.getRegionInfoByRegion(subQuery.getRegion())
                           .map(regionInfo -> new Pair<>(subQuery, regionInfo)))
        .flatMap(pair -> {
          Query subQuery = pair.getFirst();
          final Region region = subQuery.getRegion();
          final List<String> baseUrls = region.getURLs();
          final List<String> modes = subQuery.getTransportModeIds();
          final List<String> excludeStops = subQuery.getExcludedStopCodes();
          final List<String> excludedTransitModes = getExcludedTransitModesAsNonNull(
              excludedTransitModesAdapter,
              region.getName()
          );
          final Map<String, Object> options = toOptions(subQuery, pair.getSecond());
          return routingApi.fetchRoutesAsync(baseUrls, modes, excludedTransitModes, excludeStops, options);
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
  @NonNull Map<String, Object> getParamsByPreferences(RegionInfo regionInfo) {
    final ArrayMap<String, Object> map = new ArrayMap<>();
    if (tripPreferences != null) {
      if (tripPreferences.isConcessionPricingPreferred()) {
        map.put("conc", true);
      }
      if (tripPreferences.isWheelchairPreferred() && regionInfo.transitWheelchairAccessibility()) {
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

  @NonNull Map<String, Object> toOptions(@NonNull Query query, @NonNull RegionInfo regionInfo) {
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
    options.put("wp", ToWeightingProfileString.INSTANCE.toWeightingProfileString(query));
    options.putAll(getParamsByPreferences(regionInfo));
    if (extraQueryMapProvider != null) {
      final Map<String, Object> extraQueryMap = extraQueryMapProvider.call();
      options.putAll(extraQueryMap);
    }
    return options;
  }

  private Observable<Query> flatSubQueries(@NonNull Query query, @NonNull ModeFilter modeFilter) {
    return queryGenerator.call(query, modeFilter)
        .flatMap(new Func1<List<Query>, Observable<Query>>() {
          @Override
          public Observable<Query> call(List<Query> queries) {
            return Observable.from(queries);
          }
        });
  }
}