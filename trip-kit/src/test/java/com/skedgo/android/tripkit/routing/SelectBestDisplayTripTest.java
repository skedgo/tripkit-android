package com.skedgo.android.tripkit.routing;

import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;
import com.skedgo.android.tripkit.BuildConfig;
import com.skedgo.android.tripkit.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SelectBestDisplayTripTest {
  @Test public void selectDisplayTripHavingLowestWeightedScore() {
    final Trip a = new Trip();
    a.setId(0);
    a.setWeightedScore(3f);

    // This is the best display trip.
    final Trip b = new Trip();
    b.setId(1);
    b.setWeightedScore(1f);

    final Trip c = new Trip();
    c.setId(2);
    c.setWeightedScore(2f);

    final TripGroup group = new TripGroup();
    group.setTrips(new ArrayList<>(Arrays.asList(a, b, c)));

    final TripGroup actual = new SelectBestDisplayTrip().call(group);
    assertThat(actual).isNotNull().isSameAs(group);
    assertThat(actual.getTrips())
        .describedAs("Sort trips by weighted score")
        .containsExactly(b, c, a);
    assertThat(actual.getDisplayTripId())
        .describedAs("Select display trip having lowest weighted score")
        .isEqualTo(b.getId());
    assertThat(actual.getDisplayTrip())
        .describedAs("Select display trip having lowest weighted score")
        .isSameAs(b);
  }

  @Test public void doNothingIfNoTripsAvailable_null() {
    final TripGroup group = new TripGroup();
    group.setTrips(null);

    final TripGroup actual = new SelectBestDisplayTrip().call(group);
    assertThat(actual).isNotNull().isSameAs(group);
  }

  @Test public void doNothingIfNoTripsAvailable_empty() {
    final TripGroup group = new TripGroup();
    group.setTrips(new ArrayList<Trip>());

    final TripGroup actual = new SelectBestDisplayTrip().call(group);
    assertThat(actual).isNotNull().isSameAs(group);
  }
}