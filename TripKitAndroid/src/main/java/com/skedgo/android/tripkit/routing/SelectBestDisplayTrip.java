package com.skedgo.android.tripkit.routing;

import android.support.annotation.NonNull;

import skedgo.tripkit.routing.Trip;
import skedgo.tripkit.routing.TripComparators;
import com.skedgo.android.common.model.TripGroup;

import java.util.Collections;
import java.util.List;

import rx.functions.Func1;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Selects display trip which has lowest weighted score.
 * <p/>
 * If we leave after a certain query time, this helps pick
 * the best display trip having start time that is not before the query time.
 * Also, if we arrive by, it makes sure that display trip
 * having end time which is after the query time will not be selected.
 */
public final class SelectBestDisplayTrip implements Func1<TripGroup, TripGroup> {
  @NonNull @Override public TripGroup call(@NonNull TripGroup group) {
    final List<Trip> trips = group.getTrips();
    if (isNotEmpty(trips)) {
      Collections.sort(trips, TripComparators.WEIGHTED_SCORE_COMPARATOR);
      final Trip bestDisplayTrip = trips.get(0);
      return group.changeDisplayTrip(bestDisplayTrip);
    }

    return group;
  }
}