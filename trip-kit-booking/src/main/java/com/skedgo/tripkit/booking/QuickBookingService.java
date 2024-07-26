package com.skedgo.tripkit.booking;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Observable;

@Deprecated
public interface QuickBookingService {

    Observable<List<QuickBooking>> fetchQuickBookingsAsync(@NonNull String url);

}
