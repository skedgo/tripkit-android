package com.skedgo.android.common.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class BookingTest {

  @Test public void jsonKeys() {
    final Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GsonAdaptersBooking())
        .create();
    final Booking booking = ImmutableBooking.builder()
        .quickBookingsUrl("https://granduni.buzzhives.com/satapp-beta/booking/v1/8ce3829b-9f66-4ae1-87df-259202b06a27/quick")
        .title("Get a ride")
        .url("https://granduni.buzzhives.com/satapp-beta/booking/8ce3829b-9f66-4ae1-87df-259202b06a27/info")
        .externalActions(singletonList("Get a ride"))
        .build();
    final JsonObject json = gson.toJsonTree(booking).getAsJsonObject();
    assertThat(json.has("title")).isTrue();
    assertThat(json.has("externalActions")).isTrue();
    assertThat(json.has("quickBookingsUrl")).isTrue();
    assertThat(json.has("url")).isTrue();
  }
}