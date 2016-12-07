package com.skedgo.android.tripkit.booking;

import retrofit2.http.GET;
import rx.Observable;

public interface MyBookingsApi {
  @GET("booking") Observable<MyBookingsResponse> getMyBookingsAsync();
}
