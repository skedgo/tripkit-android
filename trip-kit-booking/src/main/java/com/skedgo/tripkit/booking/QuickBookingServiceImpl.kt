package com.skedgo.tripkit.booking

import io.reactivex.Observable

@Deprecated("")
class QuickBookingServiceImpl(private val api: QuickBookingApi) : QuickBookingService {
    override fun fetchQuickBookingsAsync(url: String): Observable<List<QuickBooking>> {
        return api.fetchQuickBookingsAsync(url)
    }
}
