package skedgo.tripkit.ui.module

import android.content.Context
import dagger.Module
import dagger.Provides
import skedgo.tripkit.ui.timetable.services.ServiceViewModel
import skedgo.tripkit.ui.timetable.services.ServiceViewModelImpl
import skedgo.tripkit.ui.timetable.services.uitext.GetRealtimeText
import skedgo.tripkit.ui.timetable.services.uitext.GetServiceSubTitleText
import skedgo.tripkit.ui.timetable.services.uitext.GetServiceTitleText
import skedgo.tripkit.ui.trip.details.OccupancyViewModel
import skedgo.tripkit.ui.trip.details.TimetableEntryServiceViewModel
import skedgo.tripkit.ui.trip.details.WheelchairAvailabilityViewModel

@Module
class TripkitUIModule {

  @Provides
  internal fun provideServiceViewModel(
      context: Context,
      occupancyViewModel: OccupancyViewModel,
      wheelchairAvailabilityViewModel: WheelchairAvailabilityViewModel,
      timetableEntryServiceViewModel: TimetableEntryServiceViewModel,
      getServiceTitleText: GetServiceTitleText,
      getServiceSubTitleText: GetServiceSubTitleText,
      getRealtimeText: GetRealtimeText
  ): ServiceViewModel = ServiceViewModelImpl(context,
      occupancyViewModel,
      wheelchairAvailabilityViewModel,
      timetableEntryServiceViewModel,
      getServiceTitleText,
      getServiceSubTitleText,
      getRealtimeText)

}