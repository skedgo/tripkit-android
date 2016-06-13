package com.skedgo.android.bookingclient.module;

import com.skedgo.android.bookingclient.activity.BookingActivity;
import com.skedgo.android.bookingclient.fragment.BookingFormFragment;
import com.skedgo.android.bookingclient.fragment.BookingFragment;
import com.skedgo.android.tripkit.booking.BookingModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
    modules = {BookingClientModule.class, BookingModule.class}
)
public interface BookingClientComponent {

  void inject(BookingActivity activity);
  void inject(BookingFragment fragment);
  void inject(BookingFormFragment fragment);

}