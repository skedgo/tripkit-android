package com.skedgo.android.tripkit;

import android.support.test.InstrumentationRegistry;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.common.model.TripSegment;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteServiceImplTest {
  private TripKit kit;

  @Before public void setUp() {
    TripKit.initialize(
        Configs.builder()
            .context(InstrumentationRegistry.getInstrumentation().getTargetContext())
            .regionEligibility("")
            .build()
    );
    kit = TripKit.singleton();
  }

  @Test public void getRoutes() throws Exception {
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
        .first();
    assertThat(routes).isNotNull().isNotEmpty();
    shouldSelectBestDisplayTrip(routes, TimeUnit.MILLISECONDS.toSeconds(departAfterMillis));
    assertSegments(routes);
  }

  private void shouldSelectBestDisplayTrip(List<TripGroup> routes, long departAfterSecs) {
    for (TripGroup route : routes) {
      final Trip displayTrip = route.getDisplayTrip();
      if (displayTrip != null) {
        if (displayTrip.getStartTimeInSecs() < departAfterSecs) {
          // This happens only when we find no display trip
          // whose start time is after query time. Let's check!
          final ArrayList<Trip> trips = route.getTrips();
          if (trips != null) {
            for (Trip trip : trips) {
              if (trip.getStartTimeInSecs() >= departAfterSecs) {
                Assertions.fail("Should select the best display trip");
              }
            }
          }
        }
      }
    }
  }

  private void assertSegments(List<TripGroup> routes) {
    for (TripGroup route : routes) {
      final Trip trip = route.getDisplayTrip();
      final ArrayList<TripSegment> segments = trip.getSegments();
      assertThat(segments)
          .isNotNull()
          .isNotEmpty();

      for (TripSegment segment : segments) {
        final SegmentType type = segment.getType();
        assertThat(type).isNotNull();
        if (type == SegmentType.ARRIVAL
            || type == SegmentType.DEPARTURE
            || type == SegmentType.STATIONARY) {
          assertThat(segment.getTransportModeId())
              .describedAs("Segment with type %s should have null mode id", type.name())
              .isNullOrEmpty();
        } else {
          assertThat(segment.getTransportModeId())
              .describedAs("Segment with type %s should not have null mode id", type.name())
              .isNotNull()
              .isNotEmpty();
        }
      }
    }
  }
}