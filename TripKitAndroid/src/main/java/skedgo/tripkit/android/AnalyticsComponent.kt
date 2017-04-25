package skedgo.tripkit.android

import dagger.Subcomponent
import skedgo.tripkit.analytics.AnalyticsDataModule
import skedgo.tripkit.analytics.MarkTripAsPlannedWithUserInfo
import javax.inject.Singleton

/**
 * Creates UseCases and Repositories related to the Analytics feature.
 */
@Singleton
@Subcomponent(modules = arrayOf(
    AnalyticsDataModule::class
))
interface AnalyticsComponent {
  val markTripAsPlannedWithUserInfo: MarkTripAsPlannedWithUserInfo
}