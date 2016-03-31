package com.skedgo.android.tripkit;

import rx.Observable;

public class LocationInfoServiceImpl implements LocationInfoService {

  private LocationInfoApi api;

  LocationInfoServiceImpl(LocationInfoApi api) {
    this.api = api;
  }

  @Override public Observable<LocationInfo> geLocationInfoResponseAsync(double lat, double lng) {
    return api.geLocationInfoResponseAsync("https://tripgo.skedgo.com/satapp/locationInfo.json", lat, lng);
  }
}
