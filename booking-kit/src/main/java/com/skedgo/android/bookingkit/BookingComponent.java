package com.skedgo.android.bookingkit;

import com.skedgo.android.bookingkit.api.AuthApi;
import com.skedgo.android.bookingkit.api.BookingApi;
import com.skedgo.android.bookingkit.api.QuickBookingApi;
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