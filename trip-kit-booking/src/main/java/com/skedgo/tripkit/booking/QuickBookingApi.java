package com.skedgo.tripkit.booking;

import com.skedgo.tripkit.common.model.Booking;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import io.reactivex.Observable;

@Deprecated
public interface QuickBookingApi {
  /**
   * @param quickBookingsUrl This should be obtained by {@link Booking#getQuickBookingsUrl()}.
   */
  @GET Observable<List<QuickBooking>> fetchQuickBookingsAsync(
      @Url String quickBookingsUrl
  );
}