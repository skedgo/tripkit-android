package com.skedgo.tripkit.booking;

import java.util.List;

import androidx.annotation.NonNull;
import io.reactivex.Observable;

@Deprecated
public class QuickBookingServiceImpl implements QuickBookingService {

    private final QuickBookingApi api;

    public QuickBookingServiceImpl(@NonNull QuickBookingApi api) {
        this.api = api;
    }

    @Override
    public Observable<List<QuickBooking>> fetchQuickBookingsAsync(@NonNull String url) {
        return api.fetchQuickBookingsAsync(url);
    }
}
