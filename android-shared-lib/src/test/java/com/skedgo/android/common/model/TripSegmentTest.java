package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.skedgo.android.common.BuildConfig;
import com.skedgo.android.common.TestRunner;
import com.skedgo.android.common.util.LowercaseEnumTypeAdapterFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Random;

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
  public void shouldParsePayIQConfirmationSegment() {

    String routingResponse = "{\"booking\":" +
        "{\"confirmation\":{\"status\":{\"title\":\"1min ticket\",\"subtitle\":\"Valid from Friday, 14 October, 23:01\"}," +
        "\"purchase\":{\"price\":0.009999999776482582,\"currency\":\"EUR\",\"productName\":\"1min ticket\"," +
        "\"productType\":\"pt_pub\",\"id\":\"5801398e87d4abf77d7a70bd\"}," +
        "\"actions\":[{\"title\":\"Show Ticket\",\"type\":\"QRCODE\"," +
        "\"externalURL\":\"qrcode:4f9550149c8912c040090000:5801398e87d4abf77d7a70bd:1min ticket:20161014200117:20161014200217:null:iqt\"," +
        "\"isDestructive\":false}]}," +
        "\"title\":\"1min ticket\"," +
        "\"url\":\"https://bigbang.buzzhives.com/satapp-beta/booking/v1/b1d1399a-2592-4ac2-9889-41371ddfa8ff/status?bsb=1\"}," +
        "\"endTime\":1476475680,\"segmentTemplateHashCode\":-620371530,\"serviceDirection\":\"Lentoasema\",\"serviceName\":\"Lentoasema-Satama\",\"serviceNumber\":\"1\",\"serviceTripID\":\"14_260472\",\"startTime\":1476475560,\"stops\":2,\"ticketWebsiteURL\":\"http://www.liikennevirasto.fi/\"}";

    TripSegment segment =bookingGson().fromJson(routingResponse, TripSegment.class);

    assertThat(segment.getBooking()).isNotNull();
    assertThat(segment.getBooking().getConfirmation()).isNotNull();

  }

  @Test
  public void shouldParseUberConfirmationSegment() {

    String routingResponse = "{\"booking\":" +
        "{\"confirmation\":{\"provider\":{\"title\":\"John\",\"subtitle\":\"4.9\",\"imageURL\":\"https://d1a3f4spazzrp4.cloudfront.net/uberex-sandbox/images/driver.jpg\"}," +
        "\"vehicle\":{\"title\":\"Prius Toyota\",\"subtitle\":\"UBER-PLATE\",\"imageURL\":\"https://d1a3f4spazzrp4.cloudfront.net/uberex-sandbox/images/prius.jpg\"}," +
        "\"status\":{\"title\":\"Accepted\",\"subtitle\":\"Your request has been accepted by a driver and is 'en route' to the start location.\",\"value\":\"accepted\"}," +
        "\"purchase\":{\"price\":8.0,\"currency\":\"AUD\",\"productName\":\"uberX\",\"productType\":\"ps_tnc\",\"id\":\"2d1d002b-d4d0-4411-98e1-673b244878b2\"}," +
        "\"actions\":[{\"title\":\"Cancel Ride\",\"type\":\"CANCEL\",\"internalURL\":\"https://bigbang.buzzhives.com/satapp-beta/booking/v1/9ccc7b44-2d7d-48e4-bb20-174e7adee466/cancel?bsb=1\",\"isDestructive\":true}," +
        "{\"title\":\"Call Driver\",\"type\":\"CALL\",\"externalURL\":\"tel:(555)555-5555\",\"isDestructive\":false}]},\"title\":\"Accepted\"}," +
        "\"endTime\":1476477040,\"realTime\":true,\"segmentTemplateHashCode\":105079429,\"startTime\":1476476663}";

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
}