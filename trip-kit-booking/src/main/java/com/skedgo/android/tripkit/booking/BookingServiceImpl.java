package com.skedgo.android.tripkit.booking;

import rx.Observable;
import rx.Single;

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
    return bookingApi.postFormAsync(url, inputForm);
  }

  @Override public Single<ExternalOAuth> getExternalOauth(String authId) {
    return externalOAuthStore.getExternalOauth(authId);
  }
}
