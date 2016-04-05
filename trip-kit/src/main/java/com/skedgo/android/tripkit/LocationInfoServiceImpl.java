package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;

import okhttp3.HttpUrl;
import rx.Observable;
import rx.functions.Func1;

final class LocationInfoServiceImpl implements LocationInfoService {
  private final LocationInfoApi api;
  private final RegionService regionService;

  LocationInfoServiceImpl(
      @NonNull LocationInfoApi api,
      @NonNull RegionService regionService) {
    this.api = api;
    this.regionService = regionService;
  }

  @Override public Observable<LocationInfo> getLocationInfoResponseAsync(final Location location) {
    return regionService.getRegionByLocationAsync(location)
        .flatMap(new Func1<Region, Observable<String>>() {
          @Override public Observable<String> call(Region region) {
            return Observable.from(region.getURLs());
          }
        })
        .concatMap(new Func1<String, Observable<LocationInfo>>() {
          @Override public Observable<LocationInfo> call(final String baseUrl) {
            final String url = HttpUrl.parse(baseUrl).newBuilder()
                .addPathSegment("locationInfo.json")
                .build()
                .toString();
            return api.getLocationInfoResponseAsync(
                url,
                location.getLat(),
                location.getLon()
            );
          }
        })
        .first(new Func1<LocationInfo, Boolean>() {
          @Override public Boolean call(LocationInfo response) {
            return response != null;
          }
        });
  }
}