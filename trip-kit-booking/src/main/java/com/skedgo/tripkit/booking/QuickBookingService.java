package com.skedgo.tripkit.booking;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

@Deprecated
public interface QuickBookingService {

  Observable<List<QuickBooking>> fetchQuickBookingsAsync(@NonNull String url);

}
