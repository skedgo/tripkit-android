package com.skedgo.tripkit.data.database.locations.onstreetparking

import com.skedgo.tripkit.parkingspots.OnStreetParkingRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

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