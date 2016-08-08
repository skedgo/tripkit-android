package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripTest {
  @Test public void json() {
    Trip trip = new Trip();
    trip.setUpdateURL("https://trip.go");
    trip.setProgressURL("https://trip.awesome");
    trip.setWeightedScore(5.f);
    trip.setCurrencySymbol("$M");
    trip.setCaloriesCost(12f);
    trip.setPlannedURL("https://granduni.buzzhives.com/satapp/trip/planned/cdfcc687-d577-4ba3-9a2b-a76cc289aa94");

    JsonObject jsonTrip = new Gson().toJsonTree(trip).getAsJsonObject();

    assertThat(jsonTrip.has("updateURL")).isTrue();
    assertThat(jsonTrip.has("progressURL")).isTrue();
    assertThat(jsonTrip.has("weightedScore")).isTrue();
    assertThat(jsonTrip.has("currencySymbol")).isTrue();
    assertThat(jsonTrip.has("caloriesCost")).isTrue();
    assertThat(jsonTrip.has("plannedURL")).isTrue();
  }

  @Test public void parcel() {
    Trip trip = new Trip();
    trip.setUpdateURL("https://trip.go");
    trip.setProgressURL("https://trip.awesome");
    trip.setWeightedScore(4.0f);
    trip.setCurrencySymbol("$M");
    trip.setCaloriesCost(12f);
    trip.setPlannedURL("https://granduni.buzzhives.com/satapp/trip/planned/cdfcc687-d577-4ba3-9a2b-a76cc289aa94");

    Trip actual = Trip.CREATOR.createFromParcel(Utils.parcel(trip));
    assertThat(actual.getUpdateURL()).isEqualTo(trip.getUpdateURL());
    assertThat(actual.getProgressURL()).isEqualTo(trip.getProgressURL());
    assertThat(actual.getWeightedScore()).isEqualTo(trip.getWeightedScore());
    assertThat(actual.getCurrencySymbol()).isEqualTo(trip.getCurrencySymbol());
    assertThat(actual.getCaloriesCost()).isEqualTo(trip.getCaloriesCost());
    assertThat(actual.getPlannedURL()).isEqualTo(trip.getPlannedURL());
  }

  @Test public void displayCost() {
    final Trip trip = new Trip();
    trip.setCurrencySymbol("VND");
    trip.setMoneyCost(50);
    assertThat(trip.getDisplayCost("Free")).isEqualTo("VND50");
  }

  @Test public void compareWeightedScores() {
    Trip lhs = new Trip();
    lhs.setWeightedScore(4.f);

    Trip rhs = new Trip();
    lhs.setWeightedScore(5.f);

    assertThat(Trip.Comparators.WEIGHTED_SCORE_COMPARATOR.compare(lhs, rhs)).isGreaterThan(0);
    assertThat(Trip.Comparators.WEIGHTED_SCORE_COMPARATOR.compare(lhs, lhs)).isZero();
    assertThat(Trip.Comparators.WEIGHTED_SCORE_COMPARATOR.compare(rhs, lhs)).isLessThan(0);
  }

  @Test public void timeComparatorChain() {
    Trip trip0 = new Trip();
    trip0.setStartTimeInSecs(2);
    trip0.setEndTimeInSecs(3);

    Trip trip1 = new Trip();
    trip1.setStartTimeInSecs(3);
    trip1.setEndTimeInSecs(5);

    Trip trip2 = new Trip();
    trip2.setStartTimeInSecs(3);
    trip2.setEndTimeInSecs(4);

    List<Trip> trips = new ArrayList<>(Arrays.asList(trip1, trip2, trip0));
    Collections.sort(trips, Trip.Comparators.TIME_COMPARATOR_CHAIN);

    assertThat(trips).containsExactly(trip0, trip2, trip1);
  }

  @Test public void durationComparator() {
    Trip trip0 = new Trip();
    trip0.setStartTimeInSecs(2);
    trip0.setEndTimeInSecs(3);

    Trip trip1 = new Trip();
    trip1.setStartTimeInSecs(3);
    trip1.setEndTimeInSecs(5);

    assertThat(Trip.Comparators.DURATION_COMPARATOR.compare(trip0, trip1))
        .isLessThan(0);
  }

  @Test public void moneyCostComparator() {
    {
      Trip trip0 = new Trip();
      trip0.setMoneyCost(5f);

      Trip trip1 = new Trip();
      trip1.setMoneyCost(6f);

      assertThat(Trip.Comparators.MONEY_COST_COMPARATOR.compare(trip0, trip1))
          .isLessThan(0);
    }
    {
      Trip trip0 = new Trip();
      trip0.setMoneyCost(5f);

      Trip trip1 = new Trip();
      trip1.setMoneyCost(5f);

      assertThat(Trip.Comparators.MONEY_COST_COMPARATOR.compare(trip0, trip1))
          .isZero();
    }
  }

  @Test public void carbonCostComparator() {
    {
      Trip trip0 = new Trip();
      trip0.setCarbonCost(5f);

      Trip trip1 = new Trip();
      trip1.setCarbonCost(6f);

      assertThat(Trip.Comparators.CARBON_COST_COMPARATOR.compare(trip0, trip1))
          .isLessThan(0);
    }
    {
      Trip trip0 = new Trip();
      trip0.setCarbonCost(5f);

      Trip trip1 = new Trip();
      trip1.setCarbonCost(5f);

      assertThat(Trip.Comparators.CARBON_COST_COMPARATOR.compare(trip0, trip1))
          .isZero();
    }
  }
}