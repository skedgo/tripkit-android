package com.skedgo.android.common.util;

import android.content.res.Resources;

import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;
import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripSegment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import static com.skedgo.android.common.model.TripSegment.VISIBILITY_IN_DETAILS;
import static com.skedgo.android.common.model.TripSegment.VISIBILITY_ON_MAP;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class TripSegmentListResolverTest {
  private TripSegmentListResolver resolver;

  @Before public void before() {
    final Resources resources = RuntimeEnvironment.application.getResources();
    resolver = new TripSegmentListResolver(resources);
  }

  @Test public void shouldOnlyShowDepartureSegmentInDetails() {
    TripSegment segment = new TripSegment();
    TripSegment departureSegment = resolver.createDepartureSegment(segment);
    assertThat(departureSegment.getVisibility()).isEqualTo(VISIBILITY_IN_DETAILS);
  }

  @Test public void arrivalSegmentShouldBeVisibleInMap() {
    final TripSegment segment = new TripSegment();
    final TripSegment arrivalSegment = resolver.createArrivalSegment(segment);
    assertThat(arrivalSegment.getVisibility()).isEqualTo(VISIBILITY_ON_MAP);
  }

  @Test public void shouldHaveNoTimeDifferenceForArrivalSegment() {
    TripSegment lastSegment = new TripSegment();

    // (M/D/Y @ h:m:s): 8 / 1 / 2002 @ 0:0:0 UTC
    lastSegment.setStartTimeInSecs(1028160000);

    // (M/D/Y @ h:m:s): 8 / 2 / 2002 @ 0:0:0 UTC
    lastSegment.setEndTimeInSecs(1028246400);

    TripSegment arrivalSegment = resolver.createArrivalSegment(lastSegment);

    assertThat(arrivalSegment.getType())
        .isEqualTo(SegmentType.ARRIVAL);
    assertThat(arrivalSegment.getStartTimeInSecs())
        .isEqualTo(arrivalSegment.getEndTimeInSecs())
        .isEqualTo(lastSegment.getEndTimeInSecs());
  }

  /**
   * @see <a href="https://redmine.buzzhives.com/issues/4197">Issue 4197</a>
   * @see <a href="https://redmine.buzzhives.com/issues/4397">Issue 4397</a>
   */
  @Test public void departureTimesBindsToFirstSegmentTimes() {
    TripSegment firstSegment = new TripSegment();
    firstSegment.setAction("Take MRT");
    firstSegment.setStartTimeInSecs(2);
    firstSegment.setEndTimeInSecs(3);

    TripSegment finalSegment = new TripSegment();
    finalSegment.setAction("Ride scooter");
    finalSegment.setStartTimeInSecs(3);
    finalSegment.setEndTimeInSecs(5);

    Trip trip = new Trip();
    trip.setSegments(new ArrayList<>(Arrays.asList(
        firstSegment,
        finalSegment
    )));

    resolver
        .setOrigin(new Location())
        .setDestination(new Location())
        .setTripSegmentList(trip.getSegments())
        .resolve();

    firstSegment.setStartTimeInSecs(3);
    firstSegment.setEndTimeInSecs(4);

    TripSegment departureSegment = trip.getSegments().get(0);
    assertThat(departureSegment.getStartTimeInSecs())
        .describedAs("Departure segment's start time must be bound to first segment's start time")
        .isEqualTo(firstSegment.getStartTimeInSecs());
    assertThat(departureSegment.getEndTimeInSecs())
        .describedAs("Departure segment's end time must be bound to first segment's start time")
        .isEqualTo(firstSegment.getStartTimeInSecs());
  }

  /**
   * @see <a href="https://redmine.buzzhives.com/issues/4197">Issue 4197</a>
   * @see <a href="https://redmine.buzzhives.com/issues/4397">Issue 4397</a>
   */
  @Test public void arrivalTimesBindsToFinalSegmentTimes() {
    TripSegment firstSegment = new TripSegment();
    firstSegment.setAction("Take MRT");
    firstSegment.setStartTimeInSecs(2);
    firstSegment.setEndTimeInSecs(3);

    TripSegment finalSegment = new TripSegment();
    finalSegment.setAction("Ride scooter");
    finalSegment.setStartTimeInSecs(3);
    finalSegment.setEndTimeInSecs(5);

    Trip trip = new Trip();
    trip.setSegments(new ArrayList<>(Arrays.asList(
        firstSegment,
        finalSegment
    )));

    resolver
        .setOrigin(new Location())
        .setDestination(new Location())
        .setTripSegmentList(trip.getSegments())
        .resolve();

    finalSegment.setStartTimeInSecs(4);
    finalSegment.setEndTimeInSecs(5);

    TripSegment arrivalSegment = trip.getSegments().get(trip.getSegments().size() - 1);
    assertThat(arrivalSegment.getStartTimeInSecs())
        .describedAs("Arrival segment's start time must be bound to final segment's end time")
        .isEqualTo(finalSegment.getEndTimeInSecs());
    assertThat(arrivalSegment.getEndTimeInSecs())
        .describedAs("Arrival segment's end time must be bound to final segment's end time")
        .isEqualTo(finalSegment.getEndTimeInSecs());
  }
}