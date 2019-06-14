package skedgo.tripkit.ui.module

import android.content.Context
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import skedgo.tripkit.android.TripKit
import skedgo.tripkit.ui.timetable.services.ServiceViewModel
import skedgo.tripkit.ui.timetable.services.ServiceViewModelImpl
import skedgo.tripkit.ui.timetable.services.uitext.GetRealtimeText
import skedgo.tripkit.ui.timetable.services.uitext.GetServiceSubTitleText
import skedgo.tripkit.ui.timetable.services.uitext.GetServiceTitleText
import skedgo.tripkit.ui.trip.details.OccupancyViewModel
import skedgo.tripkit.ui.trip.details.TimetableEntryServiceViewModel
import skedgo.tripkit.ui.trip.details.WheelchairAvailabilityViewModel
import javax.inject.Singleton

@Module
class TripkitUIModule(private val appContext: Context) {

  @Provides
  internal fun httpClient(): OkHttpClient {
    return TripKit.getInstance().okHttpClient3
  }

  @Provides
  internal fun provideServiceViewModel(
      occupancyViewModel: OccupancyViewModel,
      wheelchairAvailabilityViewModel: WheelchairAvailabilityViewModel,
      timetableEntryServiceViewModel: TimetableEntryServiceViewModel,
      getServiceTitleText: GetServiceTitleText,
      getServiceSubTitleText: GetServiceSubTitleText,
      getRealtimeText: GetRealtimeText
  ): ServiceViewModel = ServiceViewModelImpl(appContext,
      occupancyViewModel,
      wheelchairAvailabilityViewModel,
      timetableEntryServiceViewModel,
      getServiceTitleText,
      getServiceSubTitleText,
      getRealtimeText)

  @Provides
  @Singleton
  internal fun picasso(
      httpClient: OkHttpClient): Picasso {
    return Picasso.Builder(appContext)
        .downloader(OkHttp3Downloader(httpClient))
        .build()
  }
}