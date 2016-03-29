package com.skedgo.android.tripkit;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class RouteServiceImplTest {
  private TripKit kit;

  @Before public void before() {
    final Configs configs = Configs.builder()
        .context(InstrumentationRegistry.getInstrumentation().getTargetContext())
        .regionEligibility("")
        .debuggable(false)
        .build();
    kit = DaggerTripKit.builder()
        .tripKitModule(new TripKitModule(configs))
        .build();
  }

  @Test public void routeAsync() {
    final Location departure = new Location(-33.8599089, 151.2071104);
    final Location arrival = new Location(-33.876114, 151.205526);
    final long departAfterMillis = System.currentTimeMillis();
    final RouteOptions options = new RouteOptions.Builder(departure, arrival)
        .departAfter(departAfterMillis)
        .maxWalkingTime(10)
        .build();
    final List<TripGroup> routes = kit
        .getRouteService()
        .routeAsync(options)
        .toList()
        .toBlocking()
        .firstOrDefault(null);

    assertThat(routes).isNotNull().isNotEmpty();
    segmentsAreValid(routes);
    displayTripHasLowestWeightedScore(routes);
    tripsHaveUniqueIds(routes);
  }

  private void tripsHaveUniqueIds(List<TripGroup> routes) {
    final List<Long> ids = Observable.from(routes)
        .flatMap(new Func1<TripGroup, Observable<Trip>>() {
          @Override public Observable<Trip> call(TripGroup group) {
            return Observable.from(group.getTrips());
          }
        })
        .map(new Func1<Trip, Long>() {
          @Override public Long call(Trip trip) {
            return trip.getId();
          }
        })
        .toList()
        .toBlocking()
        .first();
    assertThat(ids).doesNotHaveDuplicates();
  }

  private void displayTripHasLowestWeightedScore(List<TripGroup> routes) {
    for (TripGroup route : routes) {
      final List<Trip> trips = route.getTrips();
      if (trips != null) {
        final Trip lowestWeightedScoreTrip = Collections
            .min(trips, new Comparator<Trip>() {
              @Override public int compare(Trip lhs, Trip rhs) {
                return Float.compare(lhs.getWeightedScore(), rhs.getWeightedScore());
              }
            });
        assertThat(route.getDisplayTrip()).isSameAs(lowestWeightedScoreTrip);
      }
    }
  }

  private void segmentsAreValid(List<TripGroup> routes) {
    for (TripGroup route : routes) {
      final Trip trip = route.getDisplayTrip();
      final ArrayList<TripSegment> segments = trip.getSegments();
      assertThat(segments).isNotNull().isNotEmpty();

      for (TripSegment segment : segments) {
        final SegmentType type = segment.getType();
        assertThat(type).isNotNull();
        if (type == SegmentType.ARRIVAL
            || type == SegmentType.DEPARTURE
            || type == SegmentType.STATIONARY) {
          assertThat(segment.getTransportModeId()).isNullOrEmpty();
        } else {
          assertThat(segment.getTransportModeId()).isNotNull().isNotEmpty();
        }
      }
    }
  }
}