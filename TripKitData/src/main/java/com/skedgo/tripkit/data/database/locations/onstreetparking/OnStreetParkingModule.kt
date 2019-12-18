package com.skedgo.tripkit.data.database.locations.onstreetparking

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import skedgo.tripkit.parkingspots.OnStreetParkingRepository

@Module
class OnStreetParkingModule {

  @Provides
  fun onStreetParkingRepository(repository: OnStreetParkingRepositoryImpl)
      : OnStreetParkingRepository = repository

  @Provides
  fun onStreetParkingApi(retrofit: Retrofit): OnStreetParkingApi {
    return retrofit.create(OnStreetParkingApi::class.java)
  }
}