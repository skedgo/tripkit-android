package com.skedgo.tripkit.data.database.locations.carparks

import com.skedgo.tripkit.data.regions.RegionService
import com.skedgo.tripkit.data.database.TripKitDatabase
import com.skedgo.tripkit.data.locations.LocationsApi
import com.skedgo.tripkit.data.locations.StopsFetcher
import dagger.Module
import dagger.Provides
import skedgo.tripkit.parkingspots.ParkingRepository

@Module
class ParkingModule {
  @Provides
  fun parkingRepository(api: LocationsApi, stopFetcher: StopsFetcher, regionService: RegionService, tripKitDatabase: TripKitDatabase):
          ParkingRepository = ParkingRepositoryImpl(api, regionService, stopFetcher, tripKitDatabase)
}