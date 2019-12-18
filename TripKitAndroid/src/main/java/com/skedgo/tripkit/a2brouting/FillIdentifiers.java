package com.skedgo.tripkit.a2brouting;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.functions.Function;
import com.skedgo.tripkit.routing.Trip;
import com.skedgo.tripkit.routing.TripGroup;

/**
 * Fills id for {@link Trip}.
 */
public final class FillIdentifiers implements Function<List<TripGroup>, List<TripGroup>> {
  private final AtomicLong idGenerator = new AtomicLong();

  @Override public List<TripGroup> apply(List<TripGroup> groups) {
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