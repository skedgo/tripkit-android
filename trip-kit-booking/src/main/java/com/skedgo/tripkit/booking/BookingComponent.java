package com.skedgo.tripkit.booking;

import com.skedgo.TripKit;
import com.skedgo.tripkit.scope.ExtensionScope;

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