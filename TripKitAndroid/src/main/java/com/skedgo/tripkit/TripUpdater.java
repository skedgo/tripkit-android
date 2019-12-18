package com.skedgo.tripkit;

import androidx.annotation.NonNull;

import skedgo.tripkit.routing.Trip;

import io.reactivex.Observable;

public interface TripUpdater {
  @NonNull Observable<Trip> getUpdateAsync(@NonNull Trip trip);
  @NonNull Observable<Trip> getUpdateAsync(@NonNull String tripUrl);
}