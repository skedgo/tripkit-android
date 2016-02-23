package com.skedgo.android.tripkit;

import com.skedgo.android.common.model.Trip;
import com.skedgo.android.common.model.TripGroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SelectBestDisplayTripTest {
  @Test
  public void shouldSelectBestDisplayTripBasedOnWeightedScore() {
    final Trip trip0 = new Trip();
    trip0.setId(0);
    trip0.setWeightedScore(1f);

    // This is the best display trip.
    final Trip trip1 = new Trip();
    trip1.setId(1);
    trip1.setWeightedScore(3f);

    final Trip trip2 = new Trip();
    trip2.setId(2);
    trip2.setWeightedScore(2f);

    final TripGroup tripGroup = new TripGroup();
    tripGroup.setTrips(new ArrayList<>(Arrays.asList(trip0, trip1, trip2)));

    final TripGroup actual = new SelectBestDisplayTrip().call(tripGroup);
    assertThat(actual).isNotNull().isSameAs(tripGroup);
    assertThat(actual.getTrips())
        .describedAs("Should sort trips by weighted score")
        .containsExactly(trip0, trip2, trip1);
    assertThat(actual.getDisplayTripId())
        .describedAs("Should select display trip having highest weighted score")
        .isEqualTo(trip1.getId());
    assertThat(actual.getDisplayTrip())
        .describedAs("Should select display trip having highest weighted score")
        .isSameAs(trip1);
  }

  @Test
  public void shouldDoNothingIfNoTripsAvailable_null() {
    final TripGroup tripGroup = new TripGroup();
    tripGroup.setTrips(null);

    final TripGroup actual = new SelectBestDisplayTrip().call(tripGroup);
    assertThat(actual).isNotNull().isSameAs(tripGroup);
  }

  @Test
  public void shouldDoNothingIfNoTripsAvailable_empty() {
    final TripGroup tripGroup = new TripGroup();
    tripGroup.setTrips(new ArrayList<Trip>());

    final TripGroup actual = new SelectBestDisplayTrip().call(tripGroup);
    assertThat(actual).isNotNull().isSameAs(tripGroup);
  }
}