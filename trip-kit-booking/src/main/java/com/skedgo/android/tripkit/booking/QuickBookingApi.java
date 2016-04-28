package com.skedgo.android.tripkit.booking;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface QuickBookingApi {
  @GET Observable<List<QuickBooking>> fetchQuickBookingsAsync(
      @Url String quickBookingsUrl
  );
}