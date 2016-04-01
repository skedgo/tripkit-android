package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Region;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

final class LocationInfoServiceImpl implements LocationInfoService {

  private LocationInfoApi api;

  LocationInfoServiceImpl(LocationInfoApi api) {
    this.api = api;
  }

  @Override public Observable<LocationInfo> getLocationInfoResponseAsync(@NonNull final Region region,
                                                                         final double lat, final double lng) {

    return Observable.from(region.getURLs())
        .concatMap(new Func1<String, Observable<LocationInfo>>() {
          @Override public Observable<LocationInfo> call(final String baseUrl) {
            return api.getLocationInfoResponseAsync(baseUrl + "/locationInfo.json", lat, lng);
          }
        })
        .first(new Func1<LocationInfo, Boolean>() {
          @Override public Boolean call(LocationInfo response) {
            return response != null;
          }
        })
        .subscribeOn(Schedulers.io());
  }
}
