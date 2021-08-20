package com.skedgo.tripkit.booking;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

@Deprecated
public class QuickBookingServiceImpl implements QuickBookingService {

  private final QuickBookingApi api;

  public QuickBookingServiceImpl(@NonNull QuickBookingApi api) {
    this.api = api;
  }

  @Override public Observable<List<QuickBooking>> fetchQuickBookingsAsync(@NonNull String url) {
    return api.fetchQuickBookingsAsync(url);
  }
}
