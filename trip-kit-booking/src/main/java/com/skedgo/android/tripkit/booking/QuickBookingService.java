package com.skedgo.android.tripkit.booking;

import androidx.annotation.NonNull;

import java.util.List;

import rx.Observable;

public interface QuickBookingService {

  Observable<List<QuickBooking>> fetchQuickBookingsAsync(@NonNull String url);

}
