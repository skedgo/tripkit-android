package com.skedgo.android.tripkit.booking;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public interface BookingService {
  Flowable<BookingForm> getFormAsync(String url);
  Flowable<BookingForm> postFormAsync(String url, InputForm inputForm);
}
