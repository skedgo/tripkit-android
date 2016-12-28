package com.skedgo.android.tripkit.booking;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(TestRunner.class)
@Config(constants = BuildConfig.class)
public class BookingServiceTest {
  @Mock BookingApi bookingApi;
  @Mock ExternalOAuthStore externalOAuthStore;
  private Gson gson;
  private BookingServiceImpl service;

  @Before public void before() {
    MockitoAnnotations.initMocks(this);
    gson = new Gson(); // use default Gson
    service = new BookingServiceImpl(bookingApi, externalOAuthStore, gson);
  }

  @Test public void shouldGetBookingFormAsync() {

    final BookingForm bookingForm = new BookingForm();
    bookingForm.setId("1");

    final Response<BookingForm> response = Response.success(bookingForm);

    when(bookingApi.getFormAsync("url"))
        .thenReturn(Observable.just(response));

    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    service.getFormAsync("url").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    BookingForm result = subscriber.getOnNextEvents().get(0);

    assertThat(bookingForm).isEqualTo(result);

  }

  @Test public void shouldGetBookingFormAsyncThrowError() {

    String responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}";

    ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), responseError);

    final Response<BookingForm> response = Response.error(401, responseBody);

    when(bookingApi.getFormAsync("url"))
        .thenReturn(Observable.just(response));

    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    service.getFormAsync("url").subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(BookingError.class);

    Throwable error = subscriber.getOnErrorEvents().get(0);

    assertThat(error.getMessage()).isEqualTo("That userToken is unrecognised.");

  }

  @Test public void shouldPostBookingFormAsync() {

    final BookingForm bookingForm = new BookingForm();
    bookingForm.setId("1");
    final InputForm inputForm = mock(InputForm.class);

    final Response<BookingForm> response = Response.success(bookingForm);

    when(bookingApi.postFormAsync("url", inputForm))
        .thenReturn(Observable.just(response));

    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    service.postFormAsync("url", inputForm).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    BookingForm result = subscriber.getOnNextEvents().get(0);

    assertThat(bookingForm).isEqualTo(result);

  }

  @Test public void shouldPostBookingFormAsyncThrowError() {

    final InputForm inputForm = mock(InputForm.class);

    String responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}";

    ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), responseError);

    final Response<BookingForm> response = Response.error(401, responseBody);

    when(bookingApi.postFormAsync("url", inputForm))
        .thenReturn(Observable.just(response));

    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    service.postFormAsync("url", inputForm).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(BookingError.class);

    Throwable error = subscriber.getOnErrorEvents().get(0);

    assertThat(error.getMessage()).isEqualTo("That userToken is unrecognised.");

  }

  @Test public void shouldHandleBookingSuccessResponse() {

    final BookingForm bookingForm = new BookingForm();
    bookingForm.setId("1");

    final Response<BookingForm> response = Response.success(bookingForm);

    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    service.handleBookingResponse.call(response).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertNoErrors();

    BookingForm result = subscriber.getOnNextEvents().get(0);

    assertThat(bookingForm).isEqualTo(result);

  }

  @Test public void shouldHandleBookingErrorResponse() {
    String responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}";

    ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), responseError);

    final Response<BookingForm> response = Response.error(401, responseBody);

    final TestSubscriber<BookingForm> subscriber = new TestSubscriber<>();

    service.handleBookingResponse.call(response).subscribe(subscriber);
    subscriber.awaitTerminalEvent();
    subscriber.assertError(BookingError.class);

    Throwable error = subscriber.getOnErrorEvents().get(0);

    assertThat(error.getMessage()).isEqualTo("That userToken is unrecognised.");

  }

  @Test public void shouldParseBookingError() {
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

  @Test public void shouldParseBookingErrorWithNullTitle() {
    String responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}";

    BookingError bookingError = gson.fromJson(responseError, BookingError.class);

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

  @Test public void shouldParseThrowableWithMessage() {
    String responseError = "{\"error\":\"That userToken is unrecognised.\",\"errorCode\":401,\"usererror\":false}";

    Throwable bookingError = gson.fromJson(responseError, BookingError.class);

    assertThat(bookingError.getMessage())
        .describedAs("Error should be read properly")
        .isEqualTo("That userToken is unrecognised.");

  }

  @Test public void shouldParseBookingErrorWithOnlyTitle() {
    String responseError = "{\"title\":\"Booking not successful\"}";
    BookingError bookingError = gson.fromJson(responseError, BookingError.class);

    assertThat(bookingError.getTitle())
        .describedAs("Title should be read properly")
        .isEqualTo("Booking not successful");
    assertThat(bookingError.getErrorCode())
        .describedAs("Error code should be default")
        .isEqualTo(0);
    assertNull(bookingError.getError());
  }
}