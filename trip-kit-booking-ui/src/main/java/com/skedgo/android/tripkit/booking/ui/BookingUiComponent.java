package com.skedgo.android.tripkit.booking.ui;

import android.databinding.DataBindingComponent;

import com.skedgo.android.tripkit.booking.BookingModule;
import com.skedgo.android.tripkit.booking.ui.activity.BookingActivity;
import com.skedgo.android.tripkit.booking.ui.activity.ExternalProviderAuthActivity;
import com.skedgo.android.tripkit.booking.ui.activity.ExternalWebActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    BookingUiModule.class,
    BookingModule.class
})
public interface BookingUiComponent extends DataBindingComponent {
  Picasso picasso();

  void inject(BookingActivity activity);
  void inject(ExternalWebActivity activity);
  void inject(ExternalProviderAuthActivity activity);

}