package com.skedgo.android.tripkit.booking.mybookings;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Observable;

public interface MyBookingsApi {
  @GET("booking")
  Observable<MyBookingsResponse> fetchMyBookingsAsync(
      @Query("first") int first,
      @Query("max") int pageSize,
      @Query("bsb") int bsb
  );
}
