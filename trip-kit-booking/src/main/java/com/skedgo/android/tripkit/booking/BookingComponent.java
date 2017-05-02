package com.skedgo.android.tripkit.booking;

import skedgo.tripkit.android.TripKit;
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
  QuickBookingApi quickBookingApi();
  AuthService authService();
  BookingService bookingService();
}