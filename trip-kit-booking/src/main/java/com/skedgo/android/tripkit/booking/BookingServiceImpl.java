package com.skedgo.android.tripkit.booking;

import java.io.IOException;

import retrofit2.Response;
import rx.Observable;
import rx.Single;
import rx.functions.Func1;

public class BookingServiceImpl implements BookingService {

  private final BookingApi bookingApi;
  private final ExternalOAuthStore externalOAuthStore;

  public BookingServiceImpl(BookingApi bookingApi, ExternalOAuthStore externalOAuthStore) {
    this.bookingApi = bookingApi;
    this.externalOAuthStore = externalOAuthStore;
  }

  @Override public Observable<BookingForm> getFormAsync(String url) {
    return bookingApi.getFormAsync(url);
  }

  @Override public Observable<BookingForm> postFormAsync(String url, InputForm inputForm) {

    return bookingApi.postFormAsync(url, inputForm)
        .flatMap(new Func1<Response<BookingForm>, Observable<BookingForm>>() {
          @Override public Observable<BookingForm> call(Response<BookingForm> bookingFormResponse) {
            if (!bookingFormResponse.isSuccessful()) {
              try {
                return Observable.error(new Error(bookingFormResponse.errorBody().string()));
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
}
