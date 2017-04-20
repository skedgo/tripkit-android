package com.skedgo.android.tripkit;

import skedgo.tripkit.routing.TripGroup;

import rx.Observable;

public interface RealTimeTripUpdateReceiver {
  Observable<TripGroup> startAsync();
  void stop();
}