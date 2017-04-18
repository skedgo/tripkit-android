package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.ModeInfo;
import com.skedgo.android.common.model.Region;
import com.skedgo.android.common.model.TransportMode;
import com.skedgo.android.tripkit.tsp.Paratransit;
import com.skedgo.android.tripkit.tsp.RegionInfo;
import com.skedgo.android.tripkit.tsp.RegionInfoService;
import skedgo.tripkit.urlresolver.GetLastUsedRegionUrls;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.inject.Provider;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;

final class RegionServiceImpl implements RegionService {
  private final Cache<List<Region>> regionCache;
  private final Cache<Map<String, TransportMode>> modeCache;
  private final RegionsFetcher regionsFetcher;
  private final Provider<RegionInfoService> regionInfoServiceProvider;
  private final RegionFinder regionFinder;
  private final GetLastUsedRegionUrls getLastUsedRegionUrls;

  RegionServiceImpl(
      Cache<List<Region>> regionCache,
      Cache<Map<String, TransportMode>> modeCache,
      @NonNull RegionsFetcher regionsFetcher,
      Provider<RegionInfoService> regionInfoServiceProvider,
      RegionFinder regionFinder,
      GetLastUsedRegionUrls getLastUsedRegionUrls) {
    this.regionCache = regionCache;
    this.modeCache = modeCache;
    this.regionsFetcher = regionsFetcher;
    this.regionInfoServiceProvider = regionInfoServiceProvider;
    this.regionFinder = regionFinder;
    this.getLastUsedRegionUrls = getLastUsedRegionUrls;
  }

  @Override
  public Observable<List<Region>> getRegionsAsync() {
    return regionCache.getAsync();
  }

  @Override
  public Observable<Map<String, TransportMode>> getTransportModesAsync() {
    return modeCache.getAsync();
  }

  @Override public Observable<Region> getRegionByLocationAsync(
      final double latitude,
      final double longitude) {
    return getRegionsAsync()
        .flatMap(new Func1<List<Region>, Observable<Region>>() {
          @Override
          public Observable<Region> call(List<Region> regions) {
            return Observable.from(regions);
          }
        })
        .first(new Func1<Region, Boolean>() {
          @Override public Boolean call(Region region) {
            return regionFinder.contains(region, latitude, longitude);
          }
        })
        .onErrorResumeNext(new Func1<Throwable, Observable<? extends Region>>() {
          @Override public Observable<? extends Region> call(Throwable error) {
            return error instanceof NoSuchElementException
                ? Observable.<Region>error(new OutOfRegionsException("Location lies outside covered area", latitude, longitude))
                : Observable.<Region>error(error);
          }
        })
        .doOnNext(new Action1<Region>() {
          @Override public void call(Region region) {
            getLastUsedRegionUrls.setLastUsedRegionUrls(region)
                .subscribe(Actions.empty(), new Action1<Throwable>() {
                  @Override public void call(Throwable throwable) {
                    Log.e("RegionServiceImpl", throwable.getMessage());
                  }
                });
          }
        });
  }

  @Override public Observable<Region> getRegionByLocationAsync(@Nullable Location location) {
    if (location == null) {
      return Observable.error(new NullPointerException("Location is null"));
    }
    return getRegionByLocationAsync(location.getLat(), location.getLon());
  }

  @Override
  public Observable<Location> getCitiesAsync() {
    return getRegionsAsync()
        .flatMap(new Func1<List<Region>, Observable<Region>>() {
          @Override
          public Observable<Region> call(List<Region> regions) {
            return Observable.from(regions);
          }
        })
        .compose(Utils.getCities());
  }

  @Override
  public Observable<Location> getCitiesByNameAsync(@Nullable String name) {
    return getCitiesAsync().filter(Utils.matchCityName(name));
  }

  @Override
  public Observable<List<TransportMode>> getTransportModesByIdsAsync(@NonNull final List<String> modeIds) {
    return getTransportModesAsync()
        .map(Utils.findModesByIds(modeIds));
  }

  @Override
  public Observable<List<TransportMode>> getTransportModesByRegionAsync(@NonNull Region region) {
    final List<String> modeIds = region.getTransportModeIds();
    return modeIds != null
        ? getTransportModesByIdsAsync(modeIds)
        : Observable.just(Collections.<TransportMode>emptyList());
  }

  @Override
  public Observable<List<TransportMode>> getTransportModesByLocationAsync(@NonNull Location location) {
    return getRegionByLocationAsync(location)
        .flatMap(new Func1<Region, Observable<List<TransportMode>>>() {
          @Override
          public Observable<List<TransportMode>> call(Region region) {
            final List<String> modeIds = region.getTransportModeIds();
            return modeIds != null
                ? getTransportModesByIdsAsync(modeIds)
                : Observable.just(Collections.<TransportMode>emptyList());
          }
        });
  }

  @Override public Observable<Void> refreshAsync() {
    return regionsFetcher.fetchAsync()
        .doOnCompleted(new Action0() {
          @Override public void call() {
            regionCache.invalidate();
            modeCache.invalidate();
            regionFinder.invalidate();
          }
        });
  }

  @Override
  public Observable<Paratransit> fetchParatransitByRegionAsync(@NonNull final Region region) {
    return getRegionInfoByRegionAsync(region)
        .map(new Func1<RegionInfo, Paratransit>() {
          @Override public Paratransit call(RegionInfo regionInfo) {
            return regionInfo.paratransit();
          }
        });
  }

  @Override
  public Observable<RegionInfo> getRegionInfoByRegionAsync(@NonNull final Region region) {
    return regionInfoServiceProvider.get()
        .fetchRegionInfoAsync(region.getURLs(), region.getName());
  }

  @Override
  public Observable<List<ModeInfo>> getTransitModesByRegionAsync(@NonNull final Region region) {
    return getRegionInfoByRegionAsync(region)
        .map(new Func1<RegionInfo, List<ModeInfo>>() {
          @Override public List<ModeInfo> call(RegionInfo regionInfo) {
            return regionInfo.transitModes();
          }
        });
  }
}