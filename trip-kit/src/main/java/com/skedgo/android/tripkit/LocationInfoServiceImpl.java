package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.Region;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

final class LocationInfoServiceImpl implements LocationInfoService {

  private final LocationInfoApi api;
  private final RegionService regionService;

  LocationInfoServiceImpl(LocationInfoApi api, RegionService regionService) {
    this.api = api;
    this.regionService = regionService;
  }

  @Override public Observable<LocationInfo> getLocationInfoResponseAsync(final Location location) {

    return regionService.getRegionByLocationAsync(location)
        .flatMap(new Func1<Region, Observable<LocationInfo>>() {
          @Override public Observable<LocationInfo> call(Region region) {
            return Observable.from(region.getURLs())
                .concatMap(new Func1<String, Observable<LocationInfo>>() {
                  @Override public Observable<LocationInfo> call(final String baseUrl) {
                    return api.getLocationInfoResponseAsync(baseUrl + "/locationInfo.json", location.getLat(), location.getLon());
                  }
                })
                .first(new Func1<LocationInfo, Boolean>() {
                  @Override public Boolean call(LocationInfo response) {
                    return response != null;
                  }
                });
          }
        })
        .first()
        .subscribeOn(Schedulers.io());

  }
}
