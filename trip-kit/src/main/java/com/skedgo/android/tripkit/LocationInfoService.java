package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Region;

import rx.Observable;

public interface LocationInfoService {
  Observable<LocationInfo> getLocationInfoResponseAsync(Region region, double lat, double lng);
}
