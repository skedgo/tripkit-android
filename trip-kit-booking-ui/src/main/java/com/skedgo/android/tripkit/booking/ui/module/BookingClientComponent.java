package com.skedgo.android.tripkit.booking.ui.module;

import com.skedgo.android.tripkit.booking.ui.OAuth2CallbackHandler;
import com.skedgo.android.tripkit.booking.ui.activity.BookingActivity;
import com.skedgo.android.tripkit.booking.ui.fragment.BookingFormFragment;
import com.skedgo.android.tripkit.booking.ui.fragment.BookingFragment;
import com.skedgo.android.tripkit.booking.ui.fragment.ExternalProviderAuthFragment;
import com.skedgo.android.tripkit.booking.BookingModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
    modules = {BookingClientModule.class, BookingModule.class}
)
public interface BookingClientComponent {

  OAuth2CallbackHandler getOAuth2CallbackHandler();

  void inject(BookingActivity activity);
  void inject(ExternalProviderAuthFragment fragment);
  void inject(BookingFragment fragment);
  void inject(BookingFormFragment fragment);

}