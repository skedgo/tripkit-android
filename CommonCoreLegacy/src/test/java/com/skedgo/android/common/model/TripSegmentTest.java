package com.skedgo.android.common.model;

import android.os.Parcel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;
import com.skedgo.android.common.util.LowercaseEnumTypeAdapterFactory;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import skedgo.tripkit.routing.SegmentType;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
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

  @Test
  public void shouldWheelchairAccessibleBeTrue() {
    JsonObject tripSegmentJson = new JsonObject();
    tripSegmentJson.addProperty("wheelchairAccessible", true);
    assertThat(new Gson().fromJson(tripSegmentJson, TripSegment.class).getWheelchairAccessible()).isTrue();
  }

  @Test
  public void shouldWheelchairAccessibleBeFalse() {
    JsonObject tripSegmentJson = new JsonObject();
    tripSegmentJson.addProperty("wheelchairAccessible", false);
    assertThat(new Gson().fromJson(tripSegmentJson, TripSegment.class).getWheelchairAccessible()).isFalse();
  }

  @Test
  public void shouldParcelTrueWheelchairAccessible() {
    TripSegment tripSegment = new TripSegment();
    tripSegment.setWheelchairAccessible(true);
    TripSegment actual = TripSegment.CREATOR.createFromParcel(Utils.parcel(tripSegment));
    assertThat(actual.getWheelchairAccessible()).isTrue();
  }

  @Test
  public void shouldParcelFalseWheelchairAccessible() {
    TripSegment tripSegment = new TripSegment();
    tripSegment.setWheelchairAccessible(false);
    TripSegment actual = TripSegment.CREATOR.createFromParcel(Utils.parcel(tripSegment));
    assertThat(actual.getWheelchairAccessible()).isFalse();
  }

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

  @Test
  public void shouldParsePayIQConfirmationSegment() throws IOException {

    String routingResponse = IOUtils.toString(getClass().getResourceAsStream("/booking-payiq.json"));

    TripSegment segment = bookingGson().fromJson(routingResponse, TripSegment.class);

    assertThat(segment.getBooking()).isNotNull();
    assertThat(segment.getBooking().getConfirmation()).isNotNull();

  }

  @Test
  public void shouldParseUberConfirmationSegment() throws IOException {

    String routingResponse = IOUtils.toString(getClass().getResourceAsStream("/booking-uber.json"));

    TripSegment segment = bookingGson().fromJson(routingResponse, TripSegment.class);

    assertThat(segment.getBooking()).isNotNull();
    assertThat(segment.getBooking().getConfirmation()).isNotNull();

  }

  private Gson bookingGson() {
    return new GsonBuilder()
        .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
        .registerTypeAdapterFactory(new GsonAdaptersBooking())
        .create();
  }

  public void shouldParseMetresAndMetresSafe() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("metres", 18);
    jsonObject.addProperty("metresSafe", 3);
    TripSegment tripSegment = new Gson().fromJson(jsonObject, TripSegment.class);
    assertThat(tripSegment.getMetres()).isEqualTo(18);
    assertThat(tripSegment.getMetresSafe()).isEqualTo(3);
  }

  @Test
  public void shouldCalculatePercentageAndRoundUp() throws Exception {
    TripSegment tripSegment = new TripSegment();
    tripSegment.setMetresSafe(10);
    tripSegment.setMetres(20);
    assertThat(tripSegment.getWheelchairFriendliness()).isEqualTo(50);
    assertThat(tripSegment.getCycleFriendliness()).isEqualTo(50);

    tripSegment.setMetresSafe(1);
    tripSegment.setMetres(3);
    assertThat(tripSegment.getWheelchairFriendliness()).isEqualTo(33);
    assertThat(tripSegment.getCycleFriendliness()).isEqualTo(33); // 1 / 3 is 33 percent

    tripSegment.setMetresSafe(2);
    tripSegment.setMetres(3);
    assertThat(tripSegment.getWheelchairFriendliness()).isEqualTo(67);
    assertThat(tripSegment.getCycleFriendliness()).isEqualTo(67); // 2 / 3 is rounded up to 67 percent
  }

  @Test
  public void shouldParcelMetres() throws Exception {
    TripSegment tripSegment = new TripSegment();
    tripSegment.setMetres(10);
    Parcel parcel = Utils.parcel(tripSegment);
    TripSegment actual = TripSegment.CREATOR.createFromParcel(parcel);
    assertThat(actual.getMetres()).isEqualTo(10);
  }

  @Test
  public void shouldParcelMetresSafe() throws Exception {
    TripSegment tripSegment = new TripSegment();
    tripSegment.setMetresSafe(17);
    Parcel parcel = Utils.parcel(tripSegment);
    TripSegment actual = TripSegment.CREATOR.createFromParcel(parcel);
    assertThat(actual.getMetresSafe()).isEqualTo(17);
  }
}