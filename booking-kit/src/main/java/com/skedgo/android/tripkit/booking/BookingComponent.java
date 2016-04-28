package com.skedgo.android.tripkit.booking;

import com.skedgo.android.tripkit.TripKit;
import com.skedgo.android.tripkit.booking.api.AuthApi;
import com.skedgo.android.tripkit.booking.api.BookingApi;
import com.skedgo.android.tripkit.booking.api.QuickBookingApi;
import com.skedgo.android.tripkit.scope.ExtensionScope;

import dagger.Component;

/**
 * To initialize this, refer to {@link DaggerBookingComponent#builder()}.
 */
@ExtensionScope
@Component(
    modules = BookingModule.class,
    dependencies = TripKit.class
)
public interface BookingComponent {
  BookingApi bookingApi();
  QuickBookingApi quickBookingApi();
  AuthApi authApi();
}