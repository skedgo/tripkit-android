package skedgo.tripkit.android

import dagger.Subcomponent
import skedgo.tripkit.analytics.AnalyticsDataModule
import skedgo.tripkit.analytics.MarkTripAsPlannedWithUserInfo

/**
 * Creates UseCases and Repositories related to the Analytics feature.
 */
@FeatureScope
@Subcomponent(modules = arrayOf(
    AnalyticsDataModule::class
))
interface AnalyticsComponent {
  val markTripAsPlannedWithUserInfo: MarkTripAsPlannedWithUserInfo
}