package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
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
}