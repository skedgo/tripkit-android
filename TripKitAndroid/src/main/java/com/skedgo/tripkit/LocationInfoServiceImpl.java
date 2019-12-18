package com.skedgo.tripkit;

import androidx.annotation.NonNull;

import com.skedgo.tripkit.common.model.Location;
import com.skedgo.tripkit.common.model.Region;

import com.skedgo.tripkit.data.regions.RegionService;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.HttpUrl;
import io.reactivex.Observable;

final class LocationInfoServiceImpl implements LocationInfoService {
  private final LocationInfoApi api;
  private final RegionService regionService;

  LocationInfoServiceImpl(
      @NonNull LocationInfoApi api,
      @NonNull RegionService regionService) {
    this.api = api;
    this.regionService = regionService;
  }

  @Override public Observable<LocationInfo> getLocationInfoAsync(final Location location) {
    return regionService.getRegionByLocationAsync(location)
        .flatMap(new Function<Region, Observable<String>>() {
          @Override public Observable<String> apply(Region region) {
            return Observable.fromIterable(region.getURLs());
          }
        })
        .concatMap(new Function<String, Observable<LocationInfo>>() {
          @Override public Observable<LocationInfo> apply(final String baseUrl) {
            final String url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("locationInfo.json")
                .build()
                .toString();
            return api.fetchLocationInfoAsync(
                url,
                location.getLat(),
                location.getLon()
            );
          }
        })
        .filter(new Predicate<LocationInfo>() {
          @Override public boolean test(LocationInfo response) {
            return response != null;
          }
        }).firstElement().toObservable();
  }
}