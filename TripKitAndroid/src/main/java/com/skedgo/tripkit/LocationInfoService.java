package com.skedgo.tripkit;

import com.skedgo.android.common.model.Location;

import io.reactivex.Observable;

public interface LocationInfoService {
  Observable<LocationInfo> getLocationInfoAsync(Location location);
}