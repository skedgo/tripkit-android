package com.skedgo.tripkit.data.database.locations.carparks

import com.skedgo.tripkit.data.database.TripKitDatabase
import com.skedgo.tripkit.data.locations.LocationsApi
import com.skedgo.tripkit.data.locations.StopsFetcher
import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.parkingspots.ParkingRepository
import dagger.Module
import dagger.Provides

@Module
class ParkingModule {
    @Provides
    fun parkingRepository(
        api: LocationsApi,
        stopFetcher: StopsFetcher,
        regionService: RegionService,
        tripKitDatabase: TripKitDatabase
    ):
        ParkingRepository = ParkingRepositoryImpl(api, regionService, stopFetcher, tripKitDatabase)
}