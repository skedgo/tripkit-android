package skedgo.tripkit.ui.module;


import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Component;
import skedgo.tripkit.realtime.RealTimeRepositoryModule;
import skedgo.tripkit.ui.timetable.services.TimetableFragment2;

@Singleton
@Component(modules = {
    TripkitUIModule.class,
    RealTimeRepositoryModule.class
})
public interface TripkitUiComponent extends androidx.databinding.DataBindingComponent {

  Picasso picasso();
  void inject(TimetableFragment2 fragment);
}