package com.skedgo.android.tripkit.booking;

import com.skedgo.android.tripkit.booking.api.AuthApi;
import com.skedgo.android.tripkit.booking.api.BookingApi;
import com.skedgo.android.tripkit.booking.api.QuickBookingApi;
import com.skedgo.android.tripkit.TripKit;
import com.skedgo.android.tripkit.scope.ExtensionScope;

import dagger.Component;

@ExtensionScope
@Component(
    modules = BookingModule.class,
    dependencies = TripKit.class
)
public abstract class BookingComponent {
  public abstract BookingApi bookingApi();
  public abstract QuickBookingApi quickBookingApi();
  public abstract AuthApi authApi();
}