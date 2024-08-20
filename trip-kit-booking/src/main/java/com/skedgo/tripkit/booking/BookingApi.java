package com.skedgo.tripkit.booking;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface BookingApi {
    @GET
    Observable<Response<BookingForm>> getFormAsync(@Url String url);

    @POST
    Observable<Response<BookingForm>> postFormAsync(
        @Url String url,
        @Body InputForm inputForm
    );

    @POST
    Observable<Response<BookingForm>> postFormAsync(@Url String url);

    @POST
    Observable<Response<BookingForm>> postFormAsync(
        @Url String url,
        @Body ActionInputForm inputForm
    );

}