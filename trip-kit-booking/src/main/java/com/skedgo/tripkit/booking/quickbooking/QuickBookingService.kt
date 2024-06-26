package com.skedgo.tripkit.booking.quickbooking

import com.skedgo.tripkit.booking.mybookings.PaymentRequest
import com.skedgo.tripkit.routing.RoutingResponse
import io.reactivex.Single
import okhttp3.ResponseBody

interface QuickBookingService {
    fun getQuickBooking(url: String): Single<List<QuickBooking>>
    fun quickBook(url: String, request: QuickBookRequest): Single<QuickBookResponse>
    fun getBookingUpdate(url: String): Single<RoutingResponse>
    fun executeBookingAction(url: String): Single<QuickBookResponse>
    fun getPaymentIntent(url: String): Single<QuickBookingPaymentIntent>
    fun postPaymentMethod(
        url: String,
        request: PaymentRequest
    ): Single<ConfirmPaymentUpdateResponse>

    fun confirmPaymentUpdate(
        method: String,
        url: String,
        request: ConfirmPaymentUpdateRequest
    ): Single<ConfirmPaymentUpdateResponse>
    fun getTicketHTML(url: String): Single<ResponseBody>
    fun getTickets(valid: Boolean): Single<List<Ticket>>
    suspend fun getTicketsAsync(valid: Boolean): List<Ticket>
    fun activateTicket(url: String): Single<ResponseBody>

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

        override fun postPaymentMethod(
            url: String,
            request: PaymentRequest
        ): Single<ConfirmPaymentUpdateResponse> =
            api.postPaymentMethod(url, request)

        override fun confirmPaymentUpdate(method: String, url: String, paymentMethod: ConfirmPaymentUpdateRequest): Single<ConfirmPaymentUpdateResponse> =
            if(method == "GET") {
                api.confirmPaymentUpdate(url)
            } else {
                api.confirmPaymentUpdate(url, paymentMethod)
            }

        override fun getTicketHTML(url: String): Single<ResponseBody> =
            api.getTicketHTML(url)

        override fun getTickets(valid: Boolean): Single<List<Ticket>> =
            api.getTickets(valid)

        override suspend fun getTicketsAsync(valid: Boolean): List<Ticket> =
            api.getTicketsAsync(valid)

        override fun activateTicket(url: String): Single<ResponseBody> =
            api.activateTicket(url)

    }
}