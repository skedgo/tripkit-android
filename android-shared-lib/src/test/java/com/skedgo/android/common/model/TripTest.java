package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
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

  @Test public void arrivalSegmentShouldNotBeInSummaryArea() {
    // Given a trip having arrival segment that is visible on the map.
    final TripSegment arrivalSegment = new TripSegment();
    arrivalSegment.setType(SegmentType.ARRIVAL);
    arrivalSegment.setVisibility(TripSegment.VISIBILITY_ON_MAP);
    final Trip trip = new Trip();
    trip.setSegments(new ArrayList<>(Collections.singletonList(arrivalSegment)));

    // We expect that arrival segment shouldn't be in the summary area.
    assertThat(trip.getSummarySegments()).isEmpty();
  }

  @Test public void summaryAreaShouldBeEmptyIfNoSegments() {
    final Trip trip = new Trip();
    assertThat(trip.getSummarySegments()).isEmpty();
  }

  @Test public void summaryAreaShouldOnlyIncludeSegmentsVisibleOnSummary() {
    final TripSegment a = mock(TripSegment.class);
    when(a.isVisibleInContext(eq(TripSegment.VISIBILITY_IN_SUMMARY)))
        .thenReturn(true);
    final TripSegment b = mock(TripSegment.class);
    when(b.isVisibleInContext(eq(TripSegment.VISIBILITY_IN_SUMMARY)))
        .thenReturn(false);
    final Trip trip = new Trip();
    trip.setSegments(new ArrayList<>(Arrays.asList(a, b)));

    // We expect that the summary area should only include
    // segments which are visible on the summary.
    assertThat(trip.getSummarySegments()).containsExactly(a);
  }
}