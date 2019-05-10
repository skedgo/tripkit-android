package com.skedgo.android.tripkit.booking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(TestRunner.class)
public class QuickBookingTest {
  @Test public void parcel() {
    final QuickBooking expected = ImmutableQuickBooking.builder()
        .bookingURL("Some booking url")
        .tripUpdateURL("Some trip update url")
        .priceInUSD(32.917942f)
        .imageURL("Some image url")
        .title("Some title")
        .subtitle("Some subtitle")
        .bookingTitle("Some booking title")
        .priceString("A$32-42")
        .price(42f)
        .eta(120f)
        .build();
    final QuickBooking actual = QuickBooking.CREATOR.createFromParcel(Parcels.parcel(expected));
    assertThat(actual).isEqualTo(expected);
  }

  @Test public void jsonKeys() {
    final QuickBooking booking = ImmutableQuickBooking.builder()
        .bookingURL("Some booking url")
        .tripUpdateURL("Some trip update url")
        .priceInUSD(32.917942f)
        .imageURL("Some image url")
        .title("Some title")
        .subtitle("Some subtitle")
        .bookingTitle("Some booking title")
        .priceString("A$32-42")
        .price(42f)
        .eta(120f)
        .build();
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersQuickBooking())
        .create();
    final JsonObject json = gson.toJsonTree(booking).getAsJsonObject();
    assertThat(json.has("bookingURL")).isTrue();
    assertThat(json.has("tripUpdateURL")).isTrue();
    assertThat(json.has("USDPrice")).isTrue();
    assertThat(json.has("imageURL")).isTrue();
    assertThat(json.has("title")).isTrue();
    assertThat(json.has("subtitle")).isTrue();
    assertThat(json.has("bookingTitle")).isTrue();
    assertThat(json.has("priceString")).isTrue();
    assertThat(json.has("price")).isTrue();
    assertThat(json.has("ETA")).isTrue();
  }
}