package com.skedgo.android.tripkit.booking;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MyBookingsApi {
  @GET("booking") Observable<MyBookingsResponse> getMyBookingsAsync(
      @Query("first") int first,
      @Query("max") int pageSize
  );
}
