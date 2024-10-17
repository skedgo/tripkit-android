package com.skedgo.tripkit.booking

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface BookingApi {
    @GET
    fun getFormAsync(@Url url: String): Observable<Response<BookingForm>>

    @POST
    fun postFormAsync(
        @Url url: String,
        @Body inputForm: InputForm
    ): Observable<Response<BookingForm>>

    @POST
    fun postFormAsync(@Url url: String): Observable<Response<BookingForm>>

    @POST
    fun postFormAsync(
        @Url url: String,
        @Body inputForm: ActionInputForm
    ): Observable<Response<BookingForm>>
}