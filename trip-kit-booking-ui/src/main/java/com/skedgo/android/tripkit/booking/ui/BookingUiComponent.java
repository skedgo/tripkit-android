package com.skedgo.android.tripkit.booking.ui;

import com.skedgo.android.tripkit.booking.BookingModule;
import com.skedgo.android.tripkit.booking.ui.activity.BookingActivity;
import com.skedgo.android.tripkit.booking.ui.activity.ExternalWebActivity;
import com.skedgo.android.tripkit.booking.ui.activity.KBookingActivity;
import com.skedgo.android.tripkit.booking.ui.fragment.BookingFormFragment;
import com.skedgo.android.tripkit.booking.ui.fragment.BookingFragment;
import com.skedgo.android.tripkit.booking.ui.fragment.ExternalProviderAuthFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    BookingUiModule.class,
    BookingModule.class
})
public interface BookingUiComponent {
  Picasso picasso();

  void inject(BookingActivity activity);
  void inject(KBookingActivity activity);
  void inject(ExternalWebActivity activity);
  void inject(ExternalProviderAuthFragment fragment);
  void inject(BookingFragment fragment);
  void inject(BookingFormFragment fragment);
}