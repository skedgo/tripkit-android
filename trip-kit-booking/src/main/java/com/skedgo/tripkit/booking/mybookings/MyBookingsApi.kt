package com.skedgo.tripkit.booking.mybookings

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MyBookingsApi {
    @GET("booking")
    fun fetchMyBookingsAsync(
        @Query("first") first: Int,
        @Query("max") pageSize: Int,
        @Query("bsb") bsb: Int
    ): Observable<MyBookingsResponse?>?
}
