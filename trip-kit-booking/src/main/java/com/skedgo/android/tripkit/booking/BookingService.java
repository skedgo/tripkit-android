package com.skedgo.android.tripkit.booking;

import rx.Observable;
import rx.Single;

public interface BookingService {

  Observable<BookingForm> getFormAsync(String url);
  Observable<BookingForm> postFormAsync(String url, InputForm inputForm);
  Single<ExternalOAuth> getExternalOauth(String authId);
}
