package com.skedgo.tripkit.validbookingcount.data

import io.reactivex.Observable
import retrofit2.http.GET

internal interface ValidBookingCountApi {
    @GET("booking/valid/count")
    fun fetchValidBookingCount(): Observable<ValidBookingCountResponse>
}
