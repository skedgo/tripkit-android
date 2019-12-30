package com.skedgo.tripkit.booking;

import io.reactivex.Flowable;

public interface BookingService {
  Flowable<BookingForm> getFormAsync(String url);
  Flowable<BookingForm> postFormAsync(String url, InputForm inputForm);
}
