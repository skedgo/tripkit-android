package com.skedgo.android.tripkit.booking;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface MyBookingsApi {
  @GET("api/booking") Observable<List<String>> getMyBookingsAsync();
}
