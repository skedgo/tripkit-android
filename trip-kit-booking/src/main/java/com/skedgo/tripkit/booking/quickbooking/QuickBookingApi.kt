package com.skedgo.tripkit.booking.quickbooking

import com.skedgo.tripkit.booking.mybookings.PaymentRequest
import com.skedgo.tripkit.routing.RoutingResponse
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
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

    @POST
    fun postPaymentMethod(@Url url: String, @Body request: PaymentRequest): Single<ConfirmPaymentUpdateResponse>

    @GET
    fun confirmPaymentUpdate(@Url url: String): Single<ConfirmPaymentUpdateResponse>
    @POST
    fun confirmPaymentUpdate(@Url url: String, @Body request: ConfirmPaymentUpdateRequest): Single<ConfirmPaymentUpdateResponse>

    @GET
    fun getTicketHTML(@Url url: String): Single<ResponseBody>

    @GET("ticket")
    fun getTickets(@Query("valid") valid: Boolean = true): Single<List<Ticket>>

    @GET("ticket")
    suspend fun getTicketsAsync(@Query("valid") valid: Boolean = true): List<Ticket>

    @POST
    fun activateTicket(@Url url: String): Single<ResponseBody>
}