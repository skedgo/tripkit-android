package com.skedgo.android.tripkit;


import kotlin.Pair;
import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;

import rx.Observable;

public interface RealTimeTripUpdateReceiver {
  Observable<Pair<Trip, TripGroup>> startAsync();
  void stop();
}