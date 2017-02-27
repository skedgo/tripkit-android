package com.skedgo.android.tripkit.booking.mybookings;

import retrofit2.http.GET;
import rx.Observable;

public interface ValidBookingCountApi {
  @GET("booking/valid/count")
  Observable<ValidBookingCountResponse> fetchValidBookingsCountAsync();
}
