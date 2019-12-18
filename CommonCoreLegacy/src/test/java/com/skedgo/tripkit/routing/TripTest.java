package com.skedgo.tripkit.routing;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
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

  @Test public void displayCost() {
    final Trip trip = new Trip();
    trip.setCurrencySymbol("VND");
    trip.setMoneyCost(50);
    assertThat(trip.getDisplayCost("Free")).isEqualTo("VND50");
  }

}