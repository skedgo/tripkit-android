package com.skedgo.android.tripkit;

import rx.Observable;

public interface LocationInfoService {
  Observable<LocationInfo> geLocationInfoResponseAsync(double lat, double lng);
}
