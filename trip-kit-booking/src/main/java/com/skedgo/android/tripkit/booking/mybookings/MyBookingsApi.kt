package com.skedgo.android.tripkit.booking.mybookings

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

interface MyBookingsApi {
  @GET("booking")
  fun fetchMyBookingsAsync(
      @Query("first") first: Int,
      @Query("max") pageSize: Int,
      @Query("bsb") bsb: Int
  ): Observable<MyBookingsResponse>
}