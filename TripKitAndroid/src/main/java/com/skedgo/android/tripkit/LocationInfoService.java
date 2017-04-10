package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Location;

import rx.Observable;

public interface LocationInfoService {
  Observable<LocationInfo> getLocationInfoAsync(Location location);
}