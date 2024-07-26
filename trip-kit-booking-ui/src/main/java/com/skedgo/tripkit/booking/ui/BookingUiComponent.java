package com.skedgo.tripkit.booking.ui;

import com.skedgo.tripkit.booking.BookingModule;
import com.skedgo.tripkit.booking.ui.activity.BookingActivity;
import com.skedgo.tripkit.booking.ui.activity.ExternalProviderAuthActivity;
import com.skedgo.tripkit.booking.ui.activity.ExternalWebActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import androidx.databinding.DataBindingComponent;
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