package com.skedgo.tripkit;


import com.skedgo.tripkit.routing.Trip;
import com.skedgo.tripkit.routing.TripGroup;

import io.reactivex.Flowable;
import kotlin.Pair;

public interface RealTimeTripUpdateReceiver {
    Flowable<Pair<Trip, TripGroup>> startAsync();

    void stop();
}