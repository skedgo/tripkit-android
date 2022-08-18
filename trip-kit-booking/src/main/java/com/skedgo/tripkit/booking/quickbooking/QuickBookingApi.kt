package com.skedgo.tripkit.booking.quickbooking

import com.skedgo.tripkit.routing.RoutingResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface QuickBookingApi {
    @GET
    fun getQuickBooking(@Url url: String): Single<List<QuickBooking>>

    @POST
    fun book(@Url url: String, @Body request: QuickBookRequest): Single<QuickBookResponse>

    @GET
    fun getBookingUpdate(@Url url: String): Single<RoutingResponse>

    @GET
    fun executeBookingAction(@Url url: String): Single<QuickBookResponse>

    @GET
    fun getPaymentIntent(@Url url: String): Single<QuickBookingPaymentIntent>
}