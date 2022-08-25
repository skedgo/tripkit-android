package com.skedgo.tripkit.booking.quickbooking

import com.skedgo.tripkit.routing.RoutingResponse
import io.reactivex.Single

interface QuickBookingService {
    fun getQuickBooking(url: String): Single<List<QuickBooking>>
    fun quickBook(url: String, request: QuickBookRequest): Single<QuickBookResponse>
    fun getBookingUpdate(url: String): Single<RoutingResponse>
    fun executeBookingAction(url: String): Single<QuickBookResponse>
    fun getPaymentIntent(url: String): Single<QuickBookingPaymentIntent>
    fun confirmPaymentUpdate(url: String): Single<ConfirmPaymentUpdateResponse>

    class QuickBookingServiceImpl(private val api: QuickBookingApi) : QuickBookingService {
        override fun getQuickBooking(url: String): Single<List<QuickBooking>> =
                api.getQuickBooking(url)

        override fun quickBook(url: String, request: QuickBookRequest): Single<QuickBookResponse> =
                api.book(url, request)

        override fun getBookingUpdate(url: String): Single<RoutingResponse> =
                api.getBookingUpdate(url)

        override fun executeBookingAction(url: String): Single<QuickBookResponse> =
                api.executeBookingAction(url)

        override fun getPaymentIntent(url: String): Single<QuickBookingPaymentIntent> =
                api.getPaymentIntent(url)

        override fun confirmPaymentUpdate(url: String): Single<ConfirmPaymentUpdateResponse> =
                api.confirmPaymentUpdate(url)
    }
}