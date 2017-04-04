package com.skedgo.android.tripkit

import dagger.Module
import dagger.Provides

@Module
class AnalyticsModule {

  @Provides fun tripIsPlannedRepository(tripIsPlannedRepositoryImpl: TripIsPlannedRepositoryImpl): TripIsPlannedRepository {
    return tripIsPlannedRepositoryImpl
  }
}