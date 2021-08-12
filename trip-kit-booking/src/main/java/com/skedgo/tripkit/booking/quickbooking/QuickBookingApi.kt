package com.skedgo.tripkit.booking.quickbooking

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface QuickBookingApi {
    @GET
    fun getQuickBooking(@Url url: String): Single<List<QuickBooking>>
}