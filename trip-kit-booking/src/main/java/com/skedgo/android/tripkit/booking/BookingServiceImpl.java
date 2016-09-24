package com.skedgo.android.tripkit.booking;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

import retrofit2.Response;
import rx.Observable;
import rx.Single;
import rx.functions.Func0;
import rx.functions.Func1;

public class BookingServiceImpl implements BookingService {

  private final BookingApi bookingApi;
  private final ExternalOAuthStore externalOAuthStore;
  private final  Gson gson;

  public BookingServiceImpl(BookingApi bookingApi, ExternalOAuthStore externalOAuthStore, Gson gson) {
    this.bookingApi = bookingApi;
    this.externalOAuthStore = externalOAuthStore;
    this.gson = gson;
  }

  @Override public Observable<BookingForm> getFormAsync(String url) {
    return bookingApi.getFormAsync(url);
  }

  @Override public Observable<BookingForm> postFormAsync(String url, InputForm inputForm) {

    return bookingApi.postFormAsync(url, inputForm)
        .flatMap(new Func1<Response<BookingForm>, Observable<BookingForm>>() {
          @Override public Observable<BookingForm> call(final Response<BookingForm> bookingFormResponse) {
            if (!bookingFormResponse.isSuccessful()) {
              try {
                return Observable.error(asBookingError(bookingFormResponse.errorBody().string()));
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
            return Observable.just(bookingFormResponse.body());
          }
        });
  }

  @Override public Single<ExternalOAuth> getExternalOauth(String authId) {
    return externalOAuthStore.getExternalOauth(authId);
  }


  private BookingError asBookingError(String bookingErrorJson) {
    final JsonReader jsonReader = new JsonReader(new StringReader(bookingErrorJson));
    return gson.fromJson(jsonReader, BookingError.class);
  }

}
