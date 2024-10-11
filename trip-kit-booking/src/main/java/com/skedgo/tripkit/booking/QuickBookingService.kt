package com.skedgo.tripkit.booking

import io.reactivex.Observable

@Deprecated("")
interface QuickBookingService {
    fun fetchQuickBookingsAsync(url: String): Observable<List<QuickBooking>>
}
