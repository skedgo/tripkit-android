package com.skedgo.tripkit.android

import com.skedgo.tripkit.analytics.AnalyticsDataModule
import com.skedgo.tripkit.analytics.MarkTripAsPlannedWithUserInfo
import dagger.Subcomponent

/**
 * Creates UseCases and Repositories related to the Analytics feature.
 */
@FeatureScope
@Subcomponent(
    modules = arrayOf(
        AnalyticsDataModule::class
    )
)
interface AnalyticsComponent {
    val markTripAsPlannedWithUserInfo: MarkTripAsPlannedWithUserInfo
}