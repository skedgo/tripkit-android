package com.skedgo.android.tripkit.booking;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface BookingApi {
  @GET Observable<BookingForm> getFormAsync(@Url String url);
  @POST Observable<Response<BookingForm>> postFormAsync(
      @Url String url,
      @Body InputForm inputForm
  );
}