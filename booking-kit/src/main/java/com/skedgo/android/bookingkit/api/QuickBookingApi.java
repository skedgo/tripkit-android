package com.skedgo.android.bookingkit.api;

import com.skedgo.android.bookingkit.model.QuickBooking;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface QuickBookingApi {
  @GET Observable<List<QuickBooking>> fetchQuickBookingsAsync(
      @Url String quickBookingsUrl
  );
}