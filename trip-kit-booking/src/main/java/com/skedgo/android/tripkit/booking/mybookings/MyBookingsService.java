package com.skedgo.android.tripkit.booking.mybookings;

import com.skedgo.android.common.model.BookingConfirmation;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class MyBookingsService {
  private final MyBookingsApi api;

  public MyBookingsService(MyBookingsApi api) {
    this.api = api;
  }

  public Observable<List<BookingConfirmation>> getMyBookingsAsync(int first, int pageSize) {
    return api.fetchMyBookingsAsync(first, pageSize)
        .map(new Func1<MyBookingsResponse, List<BookingConfirmation>>() {
          @Override public List<BookingConfirmation> call(MyBookingsResponse response) {
            return response.bookings();
          }
        });
  }
}
