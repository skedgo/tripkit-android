package com.skedgo.tripkit.data.database.timetables

import com.skedgo.tripkit.data.database.TripKitDatabase
import dagger.Module
import dagger.Provides

@Module
class ServiceAlertDataModule {

    @Provides
    internal fun serviceAlertsDao(tripGoDatabase2: TripKitDatabase): ServiceAlertsDao {
        return tripGoDatabase2.serviceAlertsDao()
    }
}