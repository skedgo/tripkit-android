package com.skedgo.android.tripkit.booking;

import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;

public class BookingServiceImpl implements BookingService {

  private final BookingApi bookingApi;
  private final Gson gson;
  @VisibleForTesting Func1<Response<BookingForm>, Observable<BookingForm>> handleBookingResponse =
      new Func1<Response<BookingForm>, Observable<BookingForm>>() {
        @Override
        public Observable<BookingForm> call(final Response<BookingForm> bookingFormResponse) {

          if (!bookingFormResponse.isSuccessful()) {
            try {
              BookingError e = asBookingError(bookingFormResponse.errorBody().string());
              return Observable.error(e);
            } catch (IOException e) {
              return Observable.error(e);
            }
          }
          return Observable.just(bookingFormResponse.body());
        }
      };

  public BookingServiceImpl(BookingApi bookingApi, Gson gson) {
    this.bookingApi = bookingApi;
    this.gson = gson;
  }

  @Override public Observable<BookingForm> getFormAsync(String url) {
    return bookingApi.getFormAsync(url).flatMap(handleBookingResponse);
  }

  @Override public Observable<BookingForm> postFormAsync(String url, InputForm inputForm) {

    return bookingApi.postFormAsync(url, inputForm)
        .flatMap(handleBookingResponse);
  }

  @VisibleForTesting BookingError asBookingError(String bookingErrorJson) {
    return gson.fromJson(bookingErrorJson, BookingError.class);
  }
}
