package com.skedgo.android.tripkit.routing;

import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripGroup;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import rx.functions.Func1;

/**
 * Fills id for {@link Trip}.
 */
public final class FillIdentifiers implements Func1<List<TripGroup>, List<TripGroup>> {
  private final AtomicLong idGenerator = new AtomicLong();

  @Override public List<TripGroup> call(List<TripGroup> groups) {
    for (TripGroup group : groups) {
      final List<Trip> trips = group.getTrips();
      if (trips != null) {
        for (Trip trip : trips) {
          trip.setId(idGenerator.incrementAndGet());
        }
      }
    }

    return groups;
  }
}