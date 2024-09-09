package com.skedgo.tripkit.booking.mybookings;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyBookingsApi {
    @GET("booking")
    Observable<MyBookingsResponse> fetchMyBookingsAsync(
        @Query("first") int first,
        @Query("max") int pageSize,
        @Query("bsb") int bsb
    );
}
