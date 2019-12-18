package com.skedgo.tripkit;

import com.skedgo.tripkit.common.model.Location;

import io.reactivex.Observable;

public interface LocationInfoService {
  Observable<LocationInfo> getLocationInfoAsync(Location location);
}