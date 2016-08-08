package com.skedgo.android.common.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TripSegmentTest {
  @Test
  public void shouldDefineCorrectSerializedNames() {
    final TripSegment segment = new TripSegment();
    segment.setServiceDirection("Foo SD");
    segment.setTransportModeId("LOL MI");
    segment.setServiceOperator("Fake SO LOL");
    segment.setTemplateHashCode(12L);
    segment.setModeInfo(new ModeInfo());
    segment.setBooking(ImmutableBooking.builder().build());
    segment.setPlatform("Platform 2");
    segment.setStopCount(7);

    final JsonObject jsonSegment = new Gson().toJsonTree(segment).getAsJsonObject();
    assertThat(jsonSegment.has("serviceDirection")).isTrue();
    assertThat(jsonSegment.has("serviceOperator")).isTrue();
    assertThat(jsonSegment.has("modeIdentifier")).isTrue();
    assertThat(jsonSegment.has("realTime")).isTrue();
    assertThat(jsonSegment.has("durationWithoutTraffic")).isTrue();
    assertThat(jsonSegment.has("segmentTemplateHashCode")).isTrue();
    assertThat(jsonSegment.has("modeInfo")).isTrue();
    assertThat(jsonSegment.has("booking")).isTrue();
    assertThat(jsonSegment.has("platform")).isTrue();
    assertThat(jsonSegment.has("stops")).isTrue();
  }

  @Test
  public void shouldNotHaveTimetableForUnscheduledSegment() {
    final TripSegment segment = new TripSegment();
    segment.setType(SegmentType.UNSCHEDULED);
    assertThat(segment.hasTimeTable()).isFalse();
  }

  @Test
  public void shouldNotHaveTimetableForStationarySegment() {
    final TripSegment segment = new TripSegment();
    segment.setType(SegmentType.STATIONARY);
    assertThat(segment.hasTimeTable()).isFalse();
  }

  @Test
  public void shouldNotHaveTimetableForArrivalSegment() {
    final TripSegment segment = new TripSegment();
    segment.setType(SegmentType.ARRIVAL);
    assertThat(segment.hasTimeTable()).isFalse();
  }

  @Test
  public void shouldNotHaveTimetableForDepartureSegment() {
    final TripSegment segment = new TripSegment();
    segment.setType(SegmentType.DEPARTURE);
    assertThat(segment.hasTimeTable()).isFalse();
  }

  @Test
  public void shouldNotHaveTimetableForContinuationSegment() {
    final TripSegment segment = new TripSegment();
    segment.setType(SegmentType.SCHEDULED);
    segment.setContinuation(true);
    assertThat(segment.hasTimeTable()).isFalse();
  }

  @Test
  public void shouldNotHaveTimetableForSegmentNotHavingServiceTripId() {
    final TripSegment segment = new TripSegment();
    segment.setType(SegmentType.SCHEDULED);
    segment.setServiceTripId(null);
    assertThat(segment.hasTimeTable()).isFalse();
  }

  @Test
  public void planeSegmentShouldNotHaveTimetable() {
    for (TripSegment segment : createSamplePlaneSegments()) {
      assertThat(segment.hasTimeTable()).isFalse();
    }
  }

  @Test
  public void shouldHaveTimetable() {
    final TripSegment segment = new TripSegment();
    segment.setType(SegmentType.SCHEDULED);
    segment.setServiceTripId("id_123");
    assertThat(segment.hasTimeTable()).isTrue();
  }

  @Test
  public void shouldBePlaneWithModeNotHavingProviderSpecified() {
    final TripSegment segment = new TripSegment();
    segment.setType(SegmentType.SCHEDULED);
    segment.setTransportModeId("in_air");
    assertThat(segment.isPlane()).isTrue();
  }

  @Test
  public void shouldBePlaneWithModeHavingProviderSpecified() {
    for (TripSegment segment : createSamplePlaneSegments()) {
      assertThat(segment.isPlane()).isTrue();
    }
  }

  @Test
  public void shouldReturnTimezoneOfDepartureLocation_nonNull() {
    final Location departure = new Location(1.0, 2.0);
    final String departureTimezone = "Australia/Sydney";
    departure.setTimeZone(departureTimezone);

    final TripSegment segment = new TripSegment();
    segment.setFrom(departure);

    assertThat(segment.getTimeZone()).isEqualTo(departureTimezone);
  }

  @Test
  public void shouldReturnTimezoneOfDepartureLocation_null() {
    final Location departure = new Location(1.0, 2.0);
    departure.setTimeZone(null);

    final TripSegment segment = new TripSegment();
    segment.setFrom(departure);

    assertThat(segment.getTimeZone()).isNull();
  }

  @Test
  public void shouldReturnTimezoneOfStationaryLocation_nonNull() {
    final Location stationary = new Location(1.0, 2.0);
    final String stationaryTimezone = "Australia/Sydney";
    stationary.setTimeZone(stationaryTimezone);

    final TripSegment segment = new TripSegment();
    segment.setSingleLocation(stationary);

    assertThat(segment.getTimeZone()).isEqualTo(stationaryTimezone);
  }

  @Test
  public void shouldReturnTimezoneOfStationaryLocation_null() {
    final Location stationary = new Location(1.0, 2.0);
    stationary.setTimeZone(null);

    final TripSegment segment = new TripSegment();
    segment.setSingleLocation(stationary);

    assertThat(segment.getTimeZone()).isNull();
  }

  @Test
  public void stopCountTextShouldBe7Stops() {
    assertThat(TripSegment.convertStopCountToText(7)).isEqualTo("7 stops");
  }

  @Test
  public void stopCountTextShouldBe1Stop() {
    assertThat(TripSegment.convertStopCountToText(1)).isEqualTo("1 stop");
  }

  @Test
  public void stopCountTextShouldBeEmpty() {
    assertThat(TripSegment.convertStopCountToText(0)).isEmpty();
  }

  @NonNull
  private ArrayList<TripSegment> createSamplePlaneSegments() {
    final ArrayList<TripSegment> planeSegments = new ArrayList<>();
    final Random random = new Random();
    for (int i = 0; i < 10; i++) {
      final String provider = String.valueOf(random.nextInt());
      final TripSegment segment = new TripSegment();
      segment.setType(SegmentType.SCHEDULED);
      segment.setServiceTripId("id_" + provider);
      segment.setTransportModeId("in_air_" + provider);
      planeSegments.add(segment);
    }

    return planeSegments;
  }
}