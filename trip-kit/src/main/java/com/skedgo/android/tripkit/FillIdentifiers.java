package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import rx.functions.Func1;

/**
 * Fills id for {@link Trip}.
 */
final class FillIdentifiers implements Func1<List<TripGroup>, List<TripGroup>> {
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