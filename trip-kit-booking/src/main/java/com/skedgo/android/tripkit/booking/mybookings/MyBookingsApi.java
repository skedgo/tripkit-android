package com.skedgo.android.tripkit.booking.mybookings;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface MyBookingsApi {
  @GET()
  Observable<MyBookingsResponse> fetchMyBookingsAsync(@Url String url,
                                                      @Query("first") int first,
                                                      @Query("max") int pageSize,
                                                      @Query("bsb") int bsb
  );
}
