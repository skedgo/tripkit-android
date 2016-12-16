package com.skedgo.android.tripkit.booking;

import android.support.annotation.NonNull;

import com.skedgo.android.common.model.BookingConfirmation;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class MyBookingsServiceImpl implements MyBookingsService {

  private final MyBookingsApi api;

  MyBookingsServiceImpl(@NonNull MyBookingsApi api) {
    this.api = api;
  }

  @Override
  public Observable<List<BookingConfirmation>> getMyBookingsAsync(int first, int pageSize) {
    return api.getMyBookingsAsync(first, pageSize)
        .map(new Func1<MyBookingsResponse, List<BookingConfirmation>>() {
          @Override public List<BookingConfirmation> call(MyBookingsResponse bookingsResponse) {
            return bookingsResponse.bookings();
          }
        });
  }

}
