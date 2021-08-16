package com.skedgo.tripkit.booking.quickbooking

import io.reactivex.Single

interface QuickBookingService {
    fun getQuickBooking(url: String): Single<List<QuickBooking>>
    fun quickBook(url: String, request: QuickBookRequest): Single<QuickBookResponse>

    class QuickBookingServiceImpl(private val api: QuickBookingApi) : QuickBookingService {
        override fun getQuickBooking(url: String): Single<List<QuickBooking>> =
                api.getQuickBooking(url)

        override fun quickBook(url: String, request: QuickBookRequest): Single<QuickBookResponse> =
                api.book(url, request)
    }
}