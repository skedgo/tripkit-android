package com.skedgo.tripkit.booking;

import com.skedgo.tripkit.common.model.booking.Booking;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

@Deprecated
public interface QuickBookingApi {
    /**
     * @param quickBookingsUrl This should be obtained by {@link Booking#getQuickBookingsUrl()}.
     */
    @GET
    Observable<List<QuickBooking>> fetchQuickBookingsAsync(
        @Url String quickBookingsUrl
    );
}