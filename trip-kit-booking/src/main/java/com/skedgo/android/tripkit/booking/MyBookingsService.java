package com.skedgo.android.tripkit.booking;

import com.skedgo.android.common.model.BookingConfirmation;

import java.util.List;

import rx.Observable;

public interface MyBookingsService {

  Observable<List<BookingConfirmation>> getMyBookingsAsync();
}
