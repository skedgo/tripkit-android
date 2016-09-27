package com.skedgo.android.tripkit.booking;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class BookingErrorTest {
  private Gson gson;
  private BookingServiceImpl service;
  @Mock BookingApi bookingApi;
  @Mock ExternalOAuthStore externalOAuthStore;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    gson = new Gson(); // use default Gson
    service = new BookingServiceImpl(bookingApi, externalOAuthStore, gson);
  }

  @Test public void Parse() {
    String responseError = "{\"errorCode\":470,\"title\":\"Booking not successful\",\"error\":\"2004 : Validation errors - length must be between 1 and 100\"}";

    BookingError bookingError = service.asBookingError(responseError);

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

  @Test public void ParseBookingService() {
    String responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}";

    JsonReader reader = new JsonReader(new StringReader(responseError));
    reader.setLenient(true);
    BookingError bookingError = gson.fromJson(reader, BookingError.class);

    assertThat(bookingError.getTitle())
        .describedAs("Title should be read null")
        .isNull();
    assertThat(bookingError.getErrorCode())
        .describedAs("Error code should be read properly")
        .isEqualTo(401);
    assertThat(bookingError.getError())
        .describedAs("Error should be read properly")
        .isEqualTo("That userToken is unrecognised.");
    assertThat(bookingError.hasUserError())
        .describedAs("User error should be false")
        .isFalse();
  }

  @Test public void ThrowableMessage() {
    String responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}";

    JsonReader reader = new JsonReader(new StringReader(responseError));
    reader.setLenient(true);
    Throwable bookingError = gson.fromJson(reader, BookingError.class);

    assertThat(bookingError.getMessage())
        .describedAs("Error should be read properly")
        .isEqualTo("That userToken is unrecognised.");

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