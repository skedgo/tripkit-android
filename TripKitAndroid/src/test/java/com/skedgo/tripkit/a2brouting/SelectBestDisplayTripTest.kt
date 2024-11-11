package com.skedgo.tripkit.a2brouting;

import com.skedgo.tripkit.routing.Trip;
import com.skedgo.tripkit.routing.TripGroup;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class SelectBestDisplayTripTest {
    @Test
    public void selectDisplayTripHavingLowestWeightedScore() {
        final Trip a = new Trip();
        a.setTripId(0);
        a.setWeightedScore(3f);

        // This is the best display trip.
        final Trip b = new Trip();
        b.setTripId(1);
        b.setWeightedScore(1f);

        final Trip c = new Trip();
        c.setTripId(2);
        c.setWeightedScore(2f);

        final TripGroup group = new TripGroup();
        group.setTrips(new ArrayList<>(Arrays.asList(a, b, c)));

        final TripGroup actual = new SelectBestDisplayTrip().apply(group);
        assertThat(actual).isNotNull().isSameAs(group);
        assertThat(actual.getTrips())
            .describedAs("Sort trips by weighted score")
            .containsExactly(b, c, a);
        assertThat(actual.getDisplayTripId())
            .describedAs("Select display trip having lowest weighted score")
            .isEqualTo(b.getTripId());
        assertThat(actual.getDisplayTrip())
            .describedAs("Select display trip having lowest weighted score")
            .isSameAs(b);
    }

    @Test
    public void doNothingIfNoTripsAvailable_null() {
        final TripGroup group = new TripGroup();
        group.setTrips(null);

        final TripGroup actual = new SelectBestDisplayTrip().apply(group);
        assertThat(actual).isNotNull().isSameAs(group);
    }

    @Test
    public void doNothingIfNoTripsAvailable_empty() {
        final TripGroup group = new TripGroup();
        group.setTrips(new ArrayList<Trip>());

        final TripGroup actual = new SelectBestDisplayTrip().apply(group);
        assertThat(actual).isNotNull().isSameAs(group);
    }
}