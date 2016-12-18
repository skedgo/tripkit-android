package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Trip;

import rx.Observable;

public interface TripUpdater {
  @NonNull Observable<Trip> getUpdateAsync(@NonNull Trip trip);
  @NonNull Observable<Trip> getUpdateAsync(@NonNull String tripUrl);
}