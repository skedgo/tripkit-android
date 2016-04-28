package com.skedgo.android.tripkit.booking;

import com.skedgo.android.tripkit.booking.model.BookingForm;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface BookingApi {
  @GET Observable<BookingForm> getFormAsync(@Url String url);
  @POST Observable<BookingForm> postFormAsync(
      @Url String url,
      @Body InputForm inputForm
  );
}