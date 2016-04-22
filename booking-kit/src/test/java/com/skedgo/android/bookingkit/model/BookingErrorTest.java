package com.skedgo.android.bookingkit.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.skedgo.android.bookingkit.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BookingErrorTest {
  private Gson gson;

  @Before public void before() {
    gson = new Gson(); // use default Gson
  }

  @Test public void Parse() {
    String responseError = "{\"errorCode\":470,\"title\":\"Booking not successful\",\"error\":\"2004 : Validation errors - length must be between 1 and 100\"}";
    JsonReader reader = new JsonReader(new StringReader(responseError));
    reader.setLenient(true);
    BookingError bookingError = gson.fromJson(reader, BookingError.class);

    assertThat(bookingError.getTitle())
        .describedAs("Title should be read properly")
        .isEqualTo("Booking not successful");
    assertThat(bookingError.getErrorCode())
        .describedAs("Error code should be read properly")
        .isEqualTo(470);
    assertThat(bookingError.getError())
        .describedAs("Error should be read properly")
        .isEqualTo("2004 : Validation errors - length must be between 1 and 100");
  }

  @Test public void Null() {
    String responseError = "{\"title\":\"Booking not successful\"}";
    JsonReader reader = new JsonReader(new StringReader(responseError));
    reader.setLenient(true);
    BookingError bookingError = gson.fromJson(reader, BookingError.class);

    assertThat(bookingError.getTitle())
        .describedAs("Title should be read properly")
        .isEqualTo("Booking not successful");
    assertThat(bookingError.getErrorCode())
        .describedAs("Error code should be default")
        .isEqualTo(0);
    assertNull(bookingError.getError());
  }
}