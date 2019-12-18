package com.skedgo.tripkit;


import io.reactivex.Flowable;
import kotlin.Pair;
import com.skedgo.tripkit.routing.Trip;
import com.skedgo.tripkit.routing.TripGroup;

public interface RealTimeTripUpdateReceiver {
  Flowable<Pair<Trip, TripGroup>> startAsync();
  void stop();
}