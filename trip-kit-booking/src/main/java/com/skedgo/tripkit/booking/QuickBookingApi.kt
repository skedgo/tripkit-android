package com.skedgo.tripkit.booking

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

@Deprecated("")
interface QuickBookingApi {
    /**
     * @param quickBookingsUrl This should be obtained by [Booking.getQuickBookingsUrl].
     */
    @GET
    fun fetchQuickBookingsAsync(
        @Url quickBookingsUrl: String
    ): Observable<List<QuickBooking>>
}