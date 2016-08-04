package com.skedgo.android.common.util;

import android.test.AndroidTestCase;

import com.skedgo.android.common.model.Location;
import com.skedgo.android.common.model.SegmentType;
import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripSegment;

import java.util.ArrayList;
import java.util.Arrays;

import static com.skedgo.android.common.model.TripSegment.VISIBILITY_IN_DETAILS;
import static org.assertj.core.api.Assertions.assertThat;

public class TripSegmentListResolverTest extends AndroidTestCase {
  public void testShouldOnlyShowDepartureSegmentInDetails() {
    TripSegment firstSegment = new TripSegment();

    TripSegmentListResolver resolver = new TripSegmentListResolver(getContext().getResources());
    TripSegment departureSegment = resolver.createDepartureSegment(firstSegment);

    assertThat(departureSegment.getVisibility()).isEqualTo(VISIBILITY_IN_DETAILS);
  }

  public void testShouldHaveNoTimeDifferenceForArrivalSegment() {
    TripSegment lastSegment = new TripSegment();

    // (M/D/Y @ h:m:s): 8 / 1 / 2002 @ 0:0:0 UTC
    lastSegment.setStartTimeInSecs(1028160000);

    // (M/D/Y @ h:m:s): 8 / 2 / 2002 @ 0:0:0 UTC
    lastSegment.setEndTimeInSecs(1028246400);

    TripSegmentListResolver testResolver = new TripSegmentListResolver(getContext().getResources());
    TripSegment arrivalSegment = testResolver.createArrivalSegment(lastSegment);

    assertThat(arrivalSegment.getType())
        .isEqualTo(SegmentType.ARRIVAL);
    assertThat(arrivalSegment.getStartTimeInSecs())
        .isEqualTo(arrivalSegment.getEndTimeInSecs())
        .isEqualTo(lastSegment.getEndTimeInSecs());
    assertFalse(arrivalSegment.isBroken());
  }

  /**
   * @see <a href="https://redmine.buzzhives.com/issues/4197">Issue 4197</a>
   * @see <a href="https://redmine.buzzhives.com/issues/4397">Issue 4397</a>
   */
  public void testDepartureTimesBindsToFirstSegmentTimes() {
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

    new TripSegmentListResolver(getContext().getResources())
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
  public void testArrivalTimesBindsToFinalSegmentTimes() {
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

    new TripSegmentListResolver(getContext().getResources())
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