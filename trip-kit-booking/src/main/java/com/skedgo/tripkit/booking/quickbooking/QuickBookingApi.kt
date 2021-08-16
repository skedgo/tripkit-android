package com.skedgo.tripkit.booking.quickbooking

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface QuickBookingApi {
    @GET
    fun getQuickBooking(@Url url: String): Single<List<QuickBooking>>

    @POST
    fun book(@Url url: String, @Body inputs: List<Input>): Single<QuickBookResponse>
}