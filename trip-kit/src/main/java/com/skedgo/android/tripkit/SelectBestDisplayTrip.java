package com.skedgo.android.tripkit;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;

import rx.functions.Func1;

/**
 * Selects display trip which has highest weighted score
 * <p/>
 * If we leave after a certain query time, this helps pick
 * the best display trip having start time that is not before the query time.
 * Also, if we arrive by, it makes sure that display trip
 * having end time which is after the query time will not be selected.
 */
public final class SelectBestDisplayTrip implements Func1<TripGroup, TripGroup> {
  @NonNull
  @Override
  public TripGroup call(@NonNull TripGroup tripGroup) {
    final ArrayList<Trip> trips = tripGroup.getTrips();
    if (CollectionUtils.isNotEmpty(trips)) {
      Collections.sort(trips, Trip.Comparators.WEIGHTED_SCORE_COMPARATOR);
      final Trip bestDisplayTrip = trips.get(trips.size() - 1);
      tripGroup.setDisplayTripId(bestDisplayTrip.getId());
    }

    return tripGroup;
  }
}